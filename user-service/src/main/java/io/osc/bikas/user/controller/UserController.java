package io.osc.bikas.user.controller;

import io.osc.bikas.user.dto.SignupRequest;
import io.osc.bikas.user.dto.SignupResponse;
import io.osc.bikas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        String userId = userService.signup(signupRequest);
        return ResponseEntity.ok(new SignupResponse(userId));
    }

}
