package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.RequestCategory;

import java.util.List;

public interface RequestCategoryService {
    List<RequestCategory> findAll();

    RequestCategory findById(long id);

    void save(RequestCategory requestCategory);
}