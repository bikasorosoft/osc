package io.osc.bikas.user.service;

import com.google.protobuf.Timestamp;
import com.osc.bikas.avro.Format;
import com.osc.bikas.avro.OtpDetails;
import com.osc.bikas.avro.UserRegistrationDetail;
import com.osc.bikas.proto.CreateUserRequest;
import com.osc.bikas.proto.GetUserPasswordByIdResponse;
import com.osc.bikas.proto.UpdatePasswordRequest;
import io.grpc.StatusRuntimeException;
import io.osc.bikas.user.exception.*;
import io.osc.bikas.user.grpc.GrpcSessionDataServiceClient;
import io.osc.bikas.user.grpc.GrpcUserDataServiceClient;
import io.osc.bikas.user.kafka.producer.OtpPublisher;
import io.osc.bikas.user.kafka.producer.RegistrationUserPublisher;
import io.osc.bikas.user.kafka.service.KafkaInteractiveQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private static final int MAX_ALLOWED_OTP_ATTEMPTS = 3;

    private final RegistrationUserPublisher registrationEventPublisher;
    private final OtpPublisher otpEventPublisher;

    private final GrpcUserDataServiceClient grpcUserDataServiceClient;
    private final GrpcSessionDataServiceClient grpcSessionDataServiceClient;

    private final KafkaInteractiveQueryService interactiveQueryService;

    public String signup(String name, String email, String contact, LocalDate dob) {

        boolean emailExists = grpcUserDataServiceClient.checkEmailExists(email);

        if(emailExists) {
            throw new EmailAlreadyInUseException(email);
        }

        String userId = generateUserId();
        UserRegistrationDetail registrationEvent = generateRegistrationUserFrom(userId, name, contact, email, dob);
        registrationEventPublisher.publish(userId, registrationEvent);

        Integer otp = generateOTP();
        OtpDetails otpEvent = generateOtpDetailsFrom(otp, email, Format.REGISTRATION);
        otpEventPublisher.publish(userId, otpEvent);

        return userId;
    }

    public void validateOTP(String userId, Integer otp) {
        ReadOnlyKeyValueStore<String, OtpDetails> store =
                interactiveQueryService.getOtpReadOnlyKeyValueStore();

        OtpDetails otpDetails = store.get(userId);

        if(otpDetails == null) {
            throw new RegistrationUserNotFoundException(userId);
        }

        int currentAttemptCount = otpDetails.getAttempts()+1;
        boolean isOtpValid = Objects.equals(otpDetails.getOtp(), otp);

        if(!isOtpValid) {
            if(currentAttemptCount >= MAX_ALLOWED_OTP_ATTEMPTS) {
                publishCleanupEvent(userId);
                throw new TooManyFailedOTPAttemptsException(userId);
            } else {
                otpDetails.setAttempts(otpDetails.getAttempts());
                otpEventPublisher.publish(userId, otpDetails);
                log.info("update attempt count for user{}", userId);
                throw new InvalidOTPException(userId);
            }
        } else {
            otpEventPublisher.publish(userId, null);
            log.info("OTP validated successfully for user {}", userId);
        }
    }

    public void addUserDetails(String userId, String password) {

        ReadOnlyKeyValueStore<String, UserRegistrationDetail> store =
                interactiveQueryService.getRegistrationUserReadOnlyKeyValueStore();

        UserRegistrationDetail registrationUserDetails = store.get(userId);

        if(registrationUserDetails == null ) {
            throw new RuntimeException("Unknown user "+userId);
        }

        CreateUserRequest createUserRequest =
                generateCreateUserRequestFrom(registrationUserDetails, userId, password);

        grpcUserDataServiceClient.createUser(createUserRequest);
        registrationEventPublisher.publish(userId, null);

    }

    public void forgotPassword(String email) {

        boolean emailExists = grpcUserDataServiceClient.checkEmailExists(email);

        if (!emailExists) {
            throw new ForgotPasswordUserNotFoundException(email);
        }

        OtpDetails otpEvent = generateOtpDetailsFrom(generateOTP(), email, Format.FORGOT_PASSWORD);

        otpEventPublisher.publish(email, otpEvent);

    }

    public void validateOTPForForgotPassword(String email, Integer otp) {
        ReadOnlyKeyValueStore<String, OtpDetails> store =
                interactiveQueryService.getOtpReadOnlyKeyValueStore();

        OtpDetails otpData = store.get(email);

        if(otpData == null) {
            throw new ForgotPasswordUserNotFoundException(email);
        }

        boolean isOtpValid = Objects.equals(otpData.getOtp(), otp);

        if(!isOtpValid) {
            throw new ForgotPasswordInvalidOTPException(email);
        }

        otpEventPublisher.publish(email, null);
        log.info("OTP validated successfully for user {}", email);

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
        String storedPassword = userPasswordAndName.getPassword();
        boolean passwordValid = storedPassword.equals(password);
        if(!passwordValid) {
            throw new LoginPasswordInvalidException(userId);
        }
        //session verification
        Boolean sessionExists = grpcSessionDataServiceClient.sessionExists(userId, deviceType);

        if(sessionExists) {
            throw new LoginSessionAlreadyExistsException(userId);
        }

        //create new session
        String sessionId = generateSessionId();
        grpcSessionDataServiceClient.createSession(sessionId, userId, deviceType);

        String name = userPasswordAndName.getName();
        Map<String, Object> response = Map.of("name", name, "sessionId", sessionId);

        return response;
    }

    public void logout(String userId, String sessionId) {
        grpcSessionDataServiceClient.logout(userId, sessionId);
    }

    private String generateUserId() {
        var num = new Random().nextInt(900000) + 100000;
        return "user"+ num;
    }

    private String generateSessionId() {
        var num = new Random().nextInt(900000) + 100000;
        return "session"+ num;// Generate random user id
    }

    private void publishCleanupEvent(String userId) {
        registrationEventPublisher.publish(userId, null);
        otpEventPublisher.publish(userId, null);
        log.info("clean data for user {}", userId);
    }

    private int generateOTP() {
        return new Random().nextInt(900000) + 100000;
    }

    private CreateUserRequest generateCreateUserRequestFrom(UserRegistrationDetail registrationUserDetails,
                                                            String userId, String password) {

        Instant instant = registrationUserDetails.getDOB().atStartOfDay().toInstant(ZoneOffset.UTC);

        return CreateUserRequest.newBuilder()
                .setId(userId)
                .setName(registrationUserDetails.getName().toString())
                .setEmail(registrationUserDetails.getEmail().toString())
                .setContactNumber(registrationUserDetails.getContact().toString())
                .setDateOfBirth(Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build())
                .setPassword(password)
                .build();
    }

    private OtpDetails generateOtpDetailsFrom(Integer otp, String email, Format format) {
        return OtpDetails.newBuilder()
                .setOtp(otp)
                .setAttempts(0)
                .setEmail(email)
                .setFormat(format)
                .build();
    }

    private UserRegistrationDetail generateRegistrationUserFrom(String userId, String name, String contact,
                                                                String email, LocalDate dob) {

        return UserRegistrationDetail.newBuilder()
                .setUserId(userId)
                .setName(name)
                .setContact(contact)
                .setEmail(email)
                .setDOB(dob)
                .build();
    }
}
