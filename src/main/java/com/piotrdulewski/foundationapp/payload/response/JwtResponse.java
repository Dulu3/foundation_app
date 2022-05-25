package com.piotrdulewski.foundationapp.payload.response;

import io.swagger.annotations.ApiModel;
import lombok.Value;

@ApiModel
@Value
public class JwtResponse {
    String token;
    String type = "Bearer";
    String refreshToken;
    String username;
    String userType;
    Long Id;
}