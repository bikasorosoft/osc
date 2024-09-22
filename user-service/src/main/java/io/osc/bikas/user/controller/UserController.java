package io.osc.bikas.user.controller;

import io.osc.bikas.user.dto.LoginRequest;
import io.osc.bikas.user.dto.*;
import io.osc.bikas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@RequestBody SignupRequest signupRequest) {
        log.info("User signup request received: {}", signupRequest);

        String name = signupRequest.getName();
        String email = signupRequest.getEmail();
        String contact = signupRequest.getContact();
        LocalDate dob = signupRequest.getDOB();

        String userId = userService.signup(name, email, contact, dob);
        Response response = new Response(200, Map.of("userId", userId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validateotp")
    public ResponseEntity<Response> validateOTP(@RequestBody ValidateOTPRequest validateOTPRequest) {
        log.info("OTP validation request for userId: {}", validateOTPRequest.getUserId());

        String userId = validateOTPRequest.getUserId();
        Integer otp = validateOTPRequest.getOtp();

        userService.validateOTP(userId, otp);
        Response response = new Response(500, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addUserDetails")
    public ResponseEntity<Response> addUserDetails(@RequestBody AddUserDetailsRequest addUserDetailsRequest) {
        log.info("Adding user details for userId: {}", addUserDetailsRequest.getUserId());

        String userId = addUserDetailsRequest.getUserId();
        String password = addUserDetailsRequest.getPassword();
        userService.addUserDetails(userId, password);
        Response response = new Response(200, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<Response> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        log.info("Forgot password request received for email: {}", forgotPasswordRequest.getEmail());

        String email = forgotPasswordRequest.getEmail();
        userService.forgotPassword(email);
        Response response = new Response(200, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validateOTPForForgotPassword")
    public ResponseEntity<Response> validateOTPForForgotPassword(@RequestBody ValidateOTPForForgotPasswordRequest request) {
        log.info("OTP validation request for forgot password for email: {}", request.getEmail());

        String email = request.getEmail();
        Integer otp = request.getOtp();

        userService.validateOTPForForgotPassword(email, otp);
        Response response = new Response(200, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Response> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        log.info("change password request for forgot password for email: {}", changePasswordRequest.getEmail());

        String email = changePasswordRequest.getEmail();
        String password = changePasswordRequest.getPassword();

        userService.changePassword(email, password);
        Response response = new Response(200, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        log.info("login request for user: {}", loginRequest.getUserId());

        String userId = loginRequest.getUserId();
        String password = loginRequest.getPassword();
        String deviceType = loginRequest.getLoginDevice();

        Map<String, Object> response = userService.login(userId, password, deviceType);
        return ResponseEntity.ok(new Response(200, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@RequestBody LogoutRequest logoutRequest) {
        log.info("logout request for user: {}", logoutRequest.getUserId());

        String userId = logoutRequest.getUserId();
        String sessionId = logoutRequest.getSessionId();

        userService.logout(userId, sessionId);

        return ResponseEntity.ok(new Response(200, null));
    }

}
