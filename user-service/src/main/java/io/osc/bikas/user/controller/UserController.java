package io.osc.bikas.user.controller;

import io.osc.bikas.user.dto.LoginRequest;
import io.osc.bikas.user.dto.*;
import io.osc.bikas.user.exception.StatusCode;
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

        String userId = userService.signup(signupRequest.getName(),
                signupRequest.getEmail().toLowerCase(),
                signupRequest.getContact(),
                signupRequest.getDOB());
        Response response = new Response(StatusCode.SUCCESS, Map.of("userId", userId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validateotp")
    public ResponseEntity<Response> validateOTP(@RequestBody ValidateOTPRequest validateOTPRequest) {
        log.info("OTP validation request for userId: {}", validateOTPRequest.getUserId());

        userService.validateOTP(validateOTPRequest.getUserId(), validateOTPRequest.getOtp());
        Response response = new Response(StatusCode.VALID_REGISTRATION_OTP, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addUserDetails")
    public ResponseEntity<Response> addUserDetails(@RequestBody AddUserDetailsRequest addUserDetailsRequest) {
        log.info("Adding user details for userId: {}", addUserDetailsRequest.getUserId());

        userService.addUserDetails(addUserDetailsRequest.getUserId(), addUserDetailsRequest.getPassword());
        Response response = new Response(StatusCode.SUCCESS, null);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<Response> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        log.info("Forgot password request received for email: {}", forgotPasswordRequest.getEmail());

        userService.forgotPassword(forgotPasswordRequest.getEmail().toLowerCase());
        Response response = new Response(StatusCode.SUCCESS, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validateOTPForForgotPassword")
    public ResponseEntity<Response> validateOTPForForgotPassword(@RequestBody ValidateOTPForForgotPasswordRequest request) {
        log.info("OTP validation request for forgot password for email: {}", request.getEmail());

        userService.validateOTPForForgotPassword(request.getEmail(), request.getOtp());
        Response response = new Response(StatusCode.SUCCESS, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Response> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        log.info("change password request for forgot password for email: {}", changePasswordRequest.getEmail());

        userService.changePassword(changePasswordRequest.getEmail(), changePasswordRequest.getPassword());
        Response response = new Response(StatusCode.SUCCESS, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        log.info("login request for user: {}", loginRequest.getUserId());

        Map<String, Object> response = userService.login(loginRequest.getUserId(), loginRequest.getPassword(), loginRequest.getLoginDevice());
        return ResponseEntity.ok(new Response(StatusCode.SUCCESS, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@RequestBody LogoutRequest logoutRequest) {
        log.info("logout request for user: {}", logoutRequest.getUserId());

        userService.logout(logoutRequest.getUserId(), logoutRequest.getSessionId());

        return ResponseEntity.ok(new Response(StatusCode.SUCCESS, null));
    }

}
