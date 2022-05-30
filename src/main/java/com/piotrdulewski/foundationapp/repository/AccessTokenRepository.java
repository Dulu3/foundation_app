package com.piotrdulewski.foundationapp.repository;

import com.piotrdulewski.foundationapp.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findById(Long id);
    Optional<AccessToken> findByToken(String token);
}
