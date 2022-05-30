package com.piotrdulewski.foundationapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Value;

@Builder
@ApiModel
@Value
public class RegisterUserDTO {
    @JsonProperty("firstName")
    String firstName;
    @JsonProperty("lastName")
    String lastName;
    @JsonProperty("email")
    String email;
    @JsonProperty("name")
    String username;
    @JsonProperty("password")
    String password;
    @JsonProperty("address")
    String address;
    @JsonProperty("logoPath")
    String logoPath;
}
