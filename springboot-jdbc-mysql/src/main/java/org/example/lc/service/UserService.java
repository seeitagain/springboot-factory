package org.example.lc.service;

import org.example.lc.entity.User;
import org.example.lc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public int saveUser(User user) {
        return userRepository.save(user);
    }
}