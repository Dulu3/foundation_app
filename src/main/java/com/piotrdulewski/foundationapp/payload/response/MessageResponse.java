package com.piotrdulewski.foundationapp.payload.response;

import io.swagger.annotations.ApiModel;
import lombok.Value;

@ApiModel
@Value
public class MessageResponse {
    String message;
}