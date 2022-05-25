package com.piotrdulewski.foundationapp.repository;

import com.piotrdulewski.foundationapp.entity.Foundation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoundationRepository extends JpaRepository<Foundation, Long> {
    Optional<Foundation> findByEmail(String email);
}
