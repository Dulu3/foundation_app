package com.piotrdulewski.foundationapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

@ApiModel
@Value
public class LoginUserDTO {
    @ApiModelProperty(value = "username", example = "iwo99@example.org")
    @JsonProperty("username")
    String username;
    @ApiModelProperty(value = "password", example = "test")
    @JsonProperty("password")
    String password;
}
