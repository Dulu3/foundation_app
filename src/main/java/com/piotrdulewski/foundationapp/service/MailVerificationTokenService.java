package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.MailVerificationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface MailVerificationTokenService {
    Optional<MailVerificationToken> findByToken(String token);

    MailVerificationToken createVerificationToken(UserDetails user);

    MailVerificationToken verifyExpiration(MailVerificationToken token);

    void delete(MailVerificationToken verificationToken);
}
