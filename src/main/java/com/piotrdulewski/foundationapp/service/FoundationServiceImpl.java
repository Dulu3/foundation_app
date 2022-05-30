package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.Foundation;
import com.piotrdulewski.foundationapp.repository.FoundationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoundationServiceImpl implements FoundationService {

    private FoundationRepository foundationRepository;

    public FoundationServiceImpl(FoundationRepository foundationRepository) {
        this.foundationRepository = foundationRepository;
    }

    @Override
    public List<Foundation> findAll() {
        return foundationRepository.findAll();
    }

    @Override

    public Foundation findById(long id) {
        Optional<Foundation> foundation = foundationRepository.findById(id);
        if (foundation.isPresent()) {
            return foundation.get();
        } else {
            throw new RuntimeException("Foundation not found id - : " + id);
        }
    }

    @Override
    public void save(Foundation foundation) {
        foundationRepository.save(foundation);
    }
}
