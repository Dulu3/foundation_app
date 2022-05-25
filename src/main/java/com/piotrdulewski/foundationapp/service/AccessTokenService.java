package com.piotrdulewski.foundationapp.service;

import com.piotrdulewski.foundationapp.entity.AccessToken;

import java.util.Optional;

public interface AccessTokenService {
    Optional<AccessToken> findByToken(String token);

    AccessToken createAccessTokenFor(Long userId, String type);

    AccessToken verifyExpiration(AccessToken token);
}
