package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.RequestCategory;
import com.piotrdulewski.foundationapp.repository.RequestCategoryRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestCategoryServiceImpl implements RequestCategoryService {

    private RequestCategoryRepository requestCategoryRepository;

    @Override
    public List<RequestCategory> findAll() {
        return requestCategoryRepository.findAll();
    }

    @Override
    public RequestCategory findById(long id) {
        Optional<RequestCategory> requestCategory = requestCategoryRepository.findById(id);
        if (requestCategory.isPresent()) {
            return requestCategory.get();

        } else {
            throw new RuntimeException("RequestCategory not found id - : " + id);
        }
    }

    @Override
    public void save(RequestCategory requestCategory) {
        requestCategoryRepository.save(requestCategory);
    }
}
