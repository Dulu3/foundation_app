package com.piotrdulewski.foundationapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterFoundationDTO {
    @JsonProperty("name")
    String name;
    @JsonProperty("NIP")
    String NIP;
    @JsonProperty("email")
    String email;
    @JsonProperty("password")
    String password;
    @JsonProperty("address")
    String address;
    @JsonProperty("logoPath")
    String logoPath;
}
