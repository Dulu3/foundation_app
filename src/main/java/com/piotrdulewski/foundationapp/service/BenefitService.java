package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.Benefit;

import java.util.List;

public interface BenefitService {
    List<Benefit> findAll();

    Benefit findById(long id);

    void save(Benefit user);
}
