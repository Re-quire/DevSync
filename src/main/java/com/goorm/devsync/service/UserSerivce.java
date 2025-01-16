package com.goorm.devsync.service;

import com.goorm.devsync.domain.user.User;
import com.goorm.devsync.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSerivce {

    private final UserRepository userRepository;

    public User createUser(String name) {
        User user = User.builder().name(name).build();
        return userRepository.save(user);
    }
}
