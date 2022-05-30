package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.repository.UserRepository;
import com.piotrdulewski.foundationapp.entity.User;

import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found id - : " + id));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
