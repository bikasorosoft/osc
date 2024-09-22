package io.osc.bikas.user.service;

import com.google.protobuf.Timestamp;
import com.osc.bikas.avro.OTPAvro;
import com.osc.bikas.avro.RegistrationUserAvro;
import com.osc.bikas.proto.CreateUserRequest;
import com.osc.bikas.proto.GetUserPasswordByIdResponse;
import com.osc.bikas.proto.UpdatePasswordRequest;
import io.grpc.StatusRuntimeException;
import io.osc.bikas.user.dto.LoginRequest;
import io.osc.bikas.user.dto.*;
import io.osc.bikas.user.exception.*;
import io.osc.bikas.user.grpc.GrpcSessionDataServiceClient;
import io.osc.bikas.user.grpc.GrpcUserDataServiceClient;
import io.osc.bikas.user.kafka.config.KafkaConstants;
import io.osc.bikas.user.kafka.producer.OTPProducer;
import io.osc.bikas.user.kafka.producer.RegistrationUserProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private static final int MAX_ATTEMPT = 2;
    private final RegistrationUserProducer registrationEventProducer;
    private final OTPProducer otpEvenProducer;

    private final GrpcUserDataServiceClient grpcUserDataServiceClient;
    private final GrpcSessionDataServiceClient grpcSessionDataServiceClient;

    private final KafkaStreamsInteractiveQueryService interactiveQueryService;

    public String signup(String name, String email, String contact, LocalDate dob) {

        // Check if email exists in user-data-service
        boolean emailExists = grpcUserDataServiceClient.checkEmailExists(email);

        if(emailExists) {
            throw new EmailAlreadyInUseException(email);
        }

        //generate user id
        String userId = generateUserId();

        // build a registration event and publish it to registration topic
        RegistrationUserAvro registrationEvent = RegistrationUserAvro.newBuilder()
                .setUserId(userId)
                .setName(name)
                .setContact(contact)
                .setEmail(email)
                .setDOB(dob)
                .build();
        registrationEventProducer.sendMessage(userId, registrationEvent);

        //generate 6 digit otp
        Integer otp = generateOTP();

        // build an otp event and publish it to otp topic
        OTPAvro otpEvent = OTPAvro.newBuilder()
                .setUserId(userId)
                .setOtp(otp)
                .setAttempts(0)
                .setName(name)
                .setEmail(email)
                .build();
        otpEvenProducer.sendMessage(userId, otpEvent);

        // Return user id
        return userId;
    }

    private int generateOTP() {
        return new Random().nextInt(900000) + 100000; // Generate 6-digit OTP
    }

    private String generateUserId() {
        var num = new Random().nextInt(900000) + 100000;
        return "user"+ num;// Generate random user id
    }

    public void validateOTP(String userId, Integer otp) {
        ReadOnlyKeyValueStore<String, OTPAvro> appStore =
                interactiveQueryService
                        .retrieveQueryableStore(
                                KafkaConstants.OTP_STORE,
                                QueryableStoreTypes.keyValueStore()
                        );

        OTPAvro otpData = appStore.get(userId);

        if(otpData == null) {
            throw new RegistrationUserNotFoundException(userId);
        }

        int attempts = otpData.getAttempts();
        boolean isOtpValid = otpData.getOtp() == otp;

        if(!isOtpValid) {
            if(attempts >= MAX_ATTEMPT) {
                publishCleanupEvent(userId);
                throw new TooManyFailedOTPAttemptsException(userId);
            } else {
                otpData.setAttempts(otpData.getAttempts()+1);
                otpEvenProducer.sendMessage(userId, otpData);
                log.info("update attempt count for user{}", userId);
                throw new InvalidOTPException(userId);
            }
        } else {
            otpEvenProducer.sendMessage(userId, null);
            log.info("OTP validated successfully for user {}", userId);
        }
    }

    private void publishCleanupEvent(String userId) {
        registrationEventProducer.sendMessage(userId, null);
        otpEvenProducer.sendMessage(userId, null);
        log.info("clean data for user {}", userId);
    }

    public void addUserDetails(String userId, String password) {

        ReadOnlyKeyValueStore<String, RegistrationUserAvro> appStore =
                interactiveQueryService
                        .retrieveQueryableStore(
                                KafkaConstants.REGISTRATION_STORE,
                                QueryableStoreTypes.keyValueStore()
                        );

        RegistrationUserAvro userAvro = appStore.get(userId);

        if(userAvro == null ) {
            throw new RuntimeException("Unknown user "+userId);
        }

        Instant instant = userAvro.getDOB().atStartOfDay().toInstant(ZoneOffset.UTC);

        CreateUserRequest createUserRequest = CreateUserRequest.newBuilder()
                .setId(userId)
                .setName(userAvro.getName().toString())
                .setEmail(userAvro.getEmail().toString())
                .setContactNumber(userAvro.getContact().toString())
                .setDateOfBirth(Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build())
                .setPassword(password)
                .build();

        grpcUserDataServiceClient.createUser(createUserRequest);

        registrationEventProducer.sendMessage(userId, null);

    }

    public void forgotPassword(String email) {

        boolean emailExists = grpcUserDataServiceClient.checkEmailExists(email);

        if (!emailExists) {
            throw new ForgotPasswordUserNotFoundException(email);
        }

        OTPAvro otpEvent = OTPAvro.newBuilder()
                .setEmail(email)
                .setOtp(generateOTP())
                .setAttempts(0)
                .build();

        otpEvenProducer.sendMessage(email, otpEvent);

    }

    public void validateOTPForForgotPassword(String email, Integer otp) {
        ReadOnlyKeyValueStore<String, OTPAvro> appStore =
                interactiveQueryService
                        .retrieveQueryableStore(
                                KafkaConstants.OTP_STORE,
                                QueryableStoreTypes.keyValueStore()
                        );

        OTPAvro otpData = appStore.get(email);

        if(otpData == null) {
            throw new ForgotPasswordUserNotFoundException(email);
        }

        boolean isOtpValid = otpData.getOtp() == otp;

        int attempts = otpData.getAttempts();

        if(!isOtpValid) {
            if(attempts >= MAX_ATTEMPT) {
                otpEvenProducer.sendMessage(email, null);
                throw new ForgotPasswordTooManyFailedOTPAttemptsException(email);
            } else {
                otpData.setAttempts(otpData.getAttempts()+1);
                otpEvenProducer.sendMessage(email, otpData);
                log.info("update attempt count for user {}", email);
                throw new ForgotPasswordInvalidOTPException(email);
            }
        } else {
            otpEvenProducer.sendMessage(email, null);
            log.info("OTP validated successfully for user {}", email);
        }
    }

    public void changePassword(String email, String password) {

        var request = UpdatePasswordRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build();
//        try{
        grpcUserDataServiceClient.updatePassword(request);
//        } catch (Exception ex) {
//            throw new ForgotPasswordUnexpectedErrorException(email);
//        }
    }

    public Map<String, Object> login(String userId, String password, String deviceType) {

        //password verification
        GetUserPasswordByIdResponse userPasswordAndName;
        try{
            userPasswordAndName = grpcUserDataServiceClient.getUserPasswordById(userId);
        } catch (StatusRuntimeException e) {
            switch (e.getStatus().getCode()) {
                case NOT_FOUND -> throw new LoginUserIdInvalidException(userId);
                default -> throw new RuntimeException(e);
            }
        }
        String password1 = userPasswordAndName.getPassword();
        boolean passwordValid = password1.equals(password);
        if(!passwordValid) {
            throw new LoginPasswordInvalidException(userId);
        }
        //session verification
        Boolean sessionExists = grpcSessionDataServiceClient.sessionExists(userId, deviceType);

        if(sessionExists) {
            //throw exception
        }

        //create new session
        String sessionId = generateSessionId();
        grpcSessionDataServiceClient.createSession(sessionId, userId, deviceType);

        String name = userPasswordAndName.getName();
        Map<String, Object> response = Map.of("name", name, "sessionId", sessionId);

        return response;
    }

    private String generateSessionId() {
        var num = new Random().nextInt(900000) + 100000;
        return "session"+ num;// Generate random user id
    }

    public void logout(String userId, String sessionId) {
        grpcSessionDataServiceClient.logout(userId, sessionId);
    }
}
