package io.osc.bikas.user.service;

import com.osc.bikas.avro.OTPAvro;
import com.osc.bikas.avro.RegistrationUserAvro;
import io.osc.bikas.user.dto.SignupRequest;
import io.osc.bikas.user.dto.ValidateOTPRequest;
import io.osc.bikas.user.exception.InvalidOTPException;
import io.osc.bikas.user.exception.MaxOTPAttemptsExceededException;
import io.osc.bikas.user.exception.UserAlreadyExists;
import io.osc.bikas.user.exception.UserIdNotFoundException;
import io.osc.bikas.user.grpc.UserDataServiceGrpcClient;
import io.osc.bikas.user.kafka.producer.OTPProducer;
import io.osc.bikas.user.kafka.producer.RegistrationUserProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.streams.KafkaStreamsInteractiveQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
            throw new UserAlreadyExists(signupRequest.getEmail());
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
                                "OTP-store",
                                QueryableStoreTypes.keyValueStore()
                        );

        String userId = validateOTPRequest.getUserId();
        OTPAvro otpData = appStore.get(userId);

        if(otpData == null) {
            throw new UserIdNotFoundException(userId);
        }

        int attempts = otpData.getAttempts();
        boolean isOtpValid = otpData.getOtp() == validateOTPRequest.getOtp();

        /*
        if(attempts >= 2 && !isOtpValid) {
            registrationEventProducer.sendMessage(userId, null);
            otpEvenProducer.sendMessage(userId, null);
            throw new MaxOTPAttemptsExceededException(userId);
        }

        if(attempts < 2 && !isOtpValid) {
            otpData.setAttempts(otpData.getAttempts()+1);
            otpEvenProducer.sendMessage(userId, otpData);
            throw new InvalidOTPException(userId);
        }

        if(attempts < 2 && isOtpValid) {
            registrationEventProducer.sendMessage(userId, null);
            otpEvenProducer.sendMessage(userId, null);
            log.info("OTP validated successfully for user {}", userId);
        }
         */
        if(!isOtpValid) {
            if(attempts >= MAX_ATTEMPT) {
                publishCleanupEvent(userId);
                throw new MaxOTPAttemptsExceededException(userId);
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
}
