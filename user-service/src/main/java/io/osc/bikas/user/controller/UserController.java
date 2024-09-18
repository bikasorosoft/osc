package io.osc.bikas.user.controller;

import io.osc.bikas.user.dto.SignupRequest;
import io.osc.bikas.user.dto.SignupResponse;
import io.osc.bikas.user.dto.ValidateOTPRequest;
import io.osc.bikas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        String userId = userService.signup(signupRequest);
        return ResponseEntity.ok(new SignupResponse(userId));
    }

//    private final KafkaStreamsInteractiveQueryService interactiveQueryService;

//    @GetMapping("/get/{userId}")
//    public void get(@PathVariable("userId") String userId) {
//        ReadOnlyKeyValueStore<String, RegistrationUserAvro> appStore =
//                interactiveQueryService
//                        .retrieveQueryableStore(
//                                "registration-store",
//                                QueryableStoreTypes.keyValueStore()
//                        );
//        log.info("{}", appStore.get(userId));
//    }

    @PostMapping("/validateotp")
    public ResponseEntity validateOTP(@RequestBody ValidateOTPRequest validateOTPRequest) {
        userService.validateOTP(validateOTPRequest);
        return new ResponseEntity<>(HttpStatusCode.valueOf(500));
    }

}
