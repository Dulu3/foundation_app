package com.piotrdulewski.foundationapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Value;

@ApiModel
@Builder
@Value
public class LoginDTO {
    @JsonProperty
    Long id;
    @JsonProperty("username")
    String username;
    @JsonProperty("type")
    String type;
}
