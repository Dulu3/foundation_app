package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.Request;

import java.util.List;

public interface RequestService {
    List<Request> findAll();

    Request findById(long id);

    void save(Request request);
}
