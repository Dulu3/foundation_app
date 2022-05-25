package com.piotrdulewski.foundationapp.payload.response;

import io.swagger.annotations.ApiModel;
import lombok.Value;

@ApiModel
@Value
public class TokenRefreshResponse {

    String accessToken;
    String refreshToken;
    String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}