package io.osc.bikas.user.service;

import com.osc.bikas.avro.OTPAvro;
import com.osc.bikas.avro.RegistrationUserAvro;
import io.osc.bikas.user.dto.SignupRequest;
import io.osc.bikas.user.exception.UserAlreadyExists;
import io.osc.bikas.user.grpc.UserDataServiceGrpcClient;
import io.osc.bikas.user.kafka.producer.OTPProducer;
import io.osc.bikas.user.kafka.producer.RegistrationUserProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RegistrationUserProducer registrationEventProducer;
    private final OTPProducer otpEvenProducer;

    private final UserDataServiceGrpcClient userDataServiceGrpcClient;

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
        registrationEventProducer.sendMessage(registrationEvent);
        // Generate OTP and publish to OTP topic
        Integer otp = generateOTP();
        OTPAvro otpEvent = OTPAvro.newBuilder()
                .setUserId(userId)
                .setOTP(otp)
                .setAttempts(0)
                .setName(signupRequest.getName())
                .setEmail(signupRequest.getEmail())
                .build();
        otpEvenProducer.sendMessage(otpEvent);

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

}
