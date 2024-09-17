package io.osc.bikas.user.data.service;

import io.osc.bikas.user.data.model.User;
import io.osc.bikas.user.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /*
    verify user email if it already exists in system during user registration
     */
    public Boolean userExists(String email){
        var user = userRepository.findByEmail(email);
        return user.isEmpty() ? Boolean.FALSE: Boolean.TRUE;
    }

    /*
    create user into db in the end of user registration process
     */
    public User create(User user){
        return userRepository.save(user);
    }

    /*
    return user password for password verification during login
     */
    public String getUserPasswordById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.get().getPassword();
    }


    public User updatePassword(String email, String password) {
        User user = userRepository.findByEmail(email).get();
        user.setPassword(password);
        return userRepository.save(user);
    }

}
