package com.piotrdulewski.foundationapp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(description = "RequestUserKey")
@Embeddable
@Getter
@Setter
public class RequestUserKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "request_Id")
    private Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestUserKey)) return false;
        RequestUserKey that = (RequestUserKey) o;
        return userId.equals(that.userId) && requestId.equals(that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, requestId);
    }
}