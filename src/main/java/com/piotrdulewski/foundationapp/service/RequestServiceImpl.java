package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.Request;
import com.piotrdulewski.foundationapp.repository.RequestRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    private RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    @Override
    public Request findById(long id) {
        Optional<Request> request = requestRepository.findById(id);
        if (request.isPresent()) {
            return request.get();

        } else {
            throw new RuntimeException("Request not found id -  " + id);
        }
    }

    @Override
    public void save(Request request) {
        requestRepository.save(request);
    }
}
