package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.advice.TokenRefreshException;
import com.piotrdulewski.foundationapp.entity.MailVerificationToken;
import com.piotrdulewski.foundationapp.repository.FoundationRepository;
import com.piotrdulewski.foundationapp.repository.MailVerificationTokenRepository;
import com.piotrdulewski.foundationapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class MailVerificationTokenServiceImpl implements MailVerificationTokenService {

    @Autowired
    private MailVerificationTokenRepository mailVerificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoundationRepository foundationRepository;


    @Override
    public Optional<MailVerificationToken> findByToken(String verificationToken) {
        return mailVerificationTokenRepository.findByToken(verificationToken);
    }

    @Override
    public MailVerificationToken createVerificationToken(UserDetails user) {
        MailVerificationToken verificationToken = new MailVerificationToken();
        verificationToken.setUserDetails(user);
        String token = UUID.randomUUID().toString();
        verificationToken.setToken(token);
        verificationToken.setCreated_at(new Timestamp(System.currentTimeMillis()));
        verificationToken.setExpire_at(new Timestamp(System.currentTimeMillis() + 24 * 3600 * 1000));
        verificationToken = mailVerificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public MailVerificationToken verifyExpiration(MailVerificationToken token) {
        if (token.getExpire_at().compareTo(new Timestamp(System.currentTimeMillis())) < 0) {
            mailVerificationTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Verification token utracil waznosc");
        }
        return token;
    }

    @Override
    public void delete(MailVerificationToken mailverificationToken) {
        mailVerificationTokenRepository.delete(mailverificationToken);
    }

}
