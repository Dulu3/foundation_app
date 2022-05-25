package com.piotrdulewski.foundationapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Value;

@ApiModel
@Value
public class UserDTO {
    @JsonProperty("username")
    String username;
    @JsonProperty("passowrd")
    String password;
}
