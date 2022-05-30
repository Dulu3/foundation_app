package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.Benefit;
import com.piotrdulewski.foundationapp.repository.BenefitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BenefitServiceImpl implements BenefitService {

    private BenefitRepository benefitRepository;

    public BenefitServiceImpl(BenefitRepository benefitRepository) {
        this.benefitRepository = benefitRepository;
    }

    @Override
    public List<Benefit> findAll() {
        return benefitRepository.findAll();
    }

    @Override
    public Benefit findById(long id) {

        Optional<Benefit> benefit = benefitRepository.findById(id);
        if (benefit.isPresent()) {
            return benefit.get();
        } else {
            throw new RuntimeException("Benefit not found id - : " + id);
        }
    }

    @Override
    public void save(Benefit benefit) {
        benefitRepository.save(benefit);
    }
}
