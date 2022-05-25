package com.piotrdulewski.foundationapp.repository;

import com.piotrdulewski.foundationapp.entity.MailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailVerificationTokenRepository extends JpaRepository<MailVerificationToken, Long> {
    Optional<MailVerificationToken> findByToken(String token);
}