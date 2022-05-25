package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.Foundation;

import java.util.List;

public interface FoundationService {
    List<Foundation> findAll();

    Foundation findById(long id);

    void save(Foundation foundation);
}