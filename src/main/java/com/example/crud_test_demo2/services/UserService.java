package com.example.crud_test_demo2.services;

import com.example.crud_test_demo2.entities.User;
import com.example.crud_test_demo2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User setUserActivationStatus(Integer userId, Boolean isActive){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return null;
        user.get().setWorking(isActive);
        return userRepository.save(user.get());
    }
}
