package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(long id);

    void save(User user);
}