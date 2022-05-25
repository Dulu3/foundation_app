package com.piotrdulewski.foundationapp.repository;

import com.piotrdulewski.foundationapp.entity.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
}
