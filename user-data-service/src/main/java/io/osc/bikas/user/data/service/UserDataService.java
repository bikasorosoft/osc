package io.osc.bikas.user.data.service;

import io.osc.bikas.user.data.exception.UnknownUserException;
import io.osc.bikas.user.data.model.User;
import io.osc.bikas.user.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDataService {

    private final UserRepository userRepository;

    public Boolean userExists(String email){
        var user = userRepository.findByEmail(email);
        return user.isEmpty() ? Boolean.FALSE: Boolean.TRUE;
    }

    public User create(User user){
        return userRepository.save(user);
    }

    public String getUserPasswordById(String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new UnknownUserException(userId));
        return user.getPassword();
    }

    public User updatePassword(String email, String password) {
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new UnknownUserException(email));
        user.setPassword(password);
        return userRepository.save(user);
    }

    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new UnknownUserException(userId));
    }
}
