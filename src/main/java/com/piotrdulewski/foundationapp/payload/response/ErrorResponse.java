package com.piotrdulewski.foundationapp.payload.response;

import io.swagger.annotations.ApiModel;
import lombok.Value;

@ApiModel
@Value
public class ErrorResponse {
    int status;
    String message;
    long timeStamp;
}