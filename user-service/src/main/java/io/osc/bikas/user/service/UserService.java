package io.osc.bikas.user.service;

import com.google.protobuf.Timestamp;
import com.osc.bikas.avro.OTPAvro;
import com.osc.bikas.avro.RegistrationUserAvro;
import com.osc.bikas.proto.CreateUserRequest;
import com.osc.bikas.proto.UpdatePasswordRequest;
import io.osc.bikas.user.dto.*;
import io.osc.bikas.user.exception.*;
import io.osc.bikas.user.grpc.UserDataServiceGrpcClient;
import io.osc.bikas.user.kafka.config.KafkaConstants;
import io.osc.bikas.user.kafka.producer.OTPProducer;
import io.osc.bikas.user.kafka.producer.RegistrationUserProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private static final int MAX_ATTEMPT = 2;
    private final RegistrationUserProducer registrationEventProducer;
    private final OTPProducer otpEvenProducer;

    private final UserDataServiceGrpcClient userDataServiceGrpcClient;

    private final KafkaStreamsInteractiveQueryService interactiveQueryService;

    public String signup(SignupRequest signupRequest) {

        // Check if email exists in user-data-service
        boolean emailExists = userDataServiceGrpcClient.checkEmailExists(signupRequest.getEmail());

        if(emailExists) {
            throw new EmailAlreadyInUseException(signupRequest.getEmail());
        }

        //generate user id
        String userId = generateUserId();

        // Publish registration data to the registration topic
        RegistrationUserAvro registrationEvent = RegistrationUserAvro.newBuilder()
                .setUserId(userId)
                .setName(signupRequest.getName())
                .setContact(signupRequest.getContact())
                .setEmail(signupRequest.getEmail())
                .setDOB(signupRequest.getDOB())
                .build();
        registrationEventProducer.sendMessage(userId, registrationEvent);
        // Generate OTP and publish to OTP topic
        Integer otp = generateOTP();
        OTPAvro otpEvent = OTPAvro.newBuilder()
                .setUserId(userId)
                .setOtp(otp)
                .setAttempts(0)
                .setName(signupRequest.getName())
                .setEmail(signupRequest.getEmail())
                .build();
        otpEvenProducer.sendMessage(userId, otpEvent);

        // Return success response
        return userId;
    }

    private int generateOTP() {
        return new Random().nextInt(900000) + 100000; // Generate 6-digit OTP
    }

    private String generateUserId() {
        var num = new Random().nextInt(900000) + 100000;
        return "user"+ num;// Generate random user id
    }

    public void validateOTP(ValidateOTPRequest validateOTPRequest) {
        ReadOnlyKeyValueStore<String, OTPAvro> appStore =
                interactiveQueryService
                        .retrieveQueryableStore(
                                KafkaConstants.OTP_STORE,
                                QueryableStoreTypes.keyValueStore()
                        );

        String userId = validateOTPRequest.getUserId();
        OTPAvro otpData = appStore.get(userId);

        if(otpData == null) {
            throw new RegistrationUserNotFoundException(userId);
        }

        int attempts = otpData.getAttempts();
        boolean isOtpValid = otpData.getOtp() == validateOTPRequest.getOtp();

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

    public void addUserDetails(AddUserDetailsRequest addUserDetailsRequest) {
        ReadOnlyKeyValueStore<String, RegistrationUserAvro> appStore =
                interactiveQueryService
                        .retrieveQueryableStore(
                                KafkaConstants.REGISTRATION_STORE,
                                QueryableStoreTypes.keyValueStore()
                        );
        String userId = addUserDetailsRequest.getUserId();
        String password = addUserDetailsRequest.getPassword();

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

        userDataServiceGrpcClient.createUser(createUserRequest);

        registrationEventProducer.sendMessage(userId, null);

    }

    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.getEmail();
        boolean emailExists = userDataServiceGrpcClient.checkEmailExists(email);
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

    public void validateOTPForForgotPassword(ValidateOTPForForgotPasswordRequest validateOTPForForgotPasswordRequest) {
        ReadOnlyKeyValueStore<String, OTPAvro> appStore =
                interactiveQueryService
                        .retrieveQueryableStore(
                                KafkaConstants.OTP_STORE,
                                QueryableStoreTypes.keyValueStore()
                        );

        String email = validateOTPForForgotPasswordRequest.getEmail();
        OTPAvro otpData = appStore.get(email);

        if(otpData == null) {
            throw new ForgotPasswordUserNotFoundException(email);
        }

        Integer otp = validateOTPForForgotPasswordRequest.getOtp();
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

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        String email = changePasswordRequest.getEmail();
        String password = changePasswordRequest.getPassword();

        var request = UpdatePasswordRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build();
//        try{
        userDataServiceGrpcClient.updatePassword(request);
//        } catch (Exception ex) {
//            throw new ForgotPasswordUnexpectedErrorException(email);
//        }
    }
}
