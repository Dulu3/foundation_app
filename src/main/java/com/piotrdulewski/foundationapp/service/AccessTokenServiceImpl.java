package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.advice.TokenRefreshException;
import com.piotrdulewski.foundationapp.entity.AccessToken;
import com.piotrdulewski.foundationapp.repository.AccessTokenRepository;
import com.piotrdulewski.foundationapp.repository.FoundationRepository;
import com.piotrdulewski.foundationapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoundationRepository foundationRepository;

    @Override
    public Optional<AccessToken> findByToken(String token) {
        return accessTokenRepository.findByToken(token);
    }

    @Override
    public AccessToken createAccessTokenFor(Long userId, String type) {
        AccessToken accessToken = new AccessToken();
        switch (type) {
            case "user":
                accessToken.setUserDetails(userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found - : " + userId)));
                break;
            case "foundation":
                accessToken.setUserDetails(foundationRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Foundation not found - : " + userId)));
                break;
        }
        accessToken.setCratedAt(new Timestamp(System.currentTimeMillis()));
        accessToken.setExpiryAt(new Timestamp(System.currentTimeMillis() + 24 * 360 * 1000));
        accessToken.setToken(UUID.randomUUID().toString());
        accessToken = accessTokenRepository.save(accessToken);
        return accessToken;
    }

    @Override
    public AccessToken verifyExpiration(AccessToken token) {
        if (token.getExpiryAt().compareTo(new Timestamp(System.currentTimeMillis())) < 0) {
            accessTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token utracil waznosc");
        }
        return token;
    }
}
