package com.piotrdulewski.foundationapp.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class FoundationUserKey implements Serializable {

    @Column(name = "foundation_id")
    private Long foundationId;

    @Column(name = "user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoundationUserKey)) return false;
        FoundationUserKey that = (FoundationUserKey) o;
        return Objects.equals(foundationId, that.foundationId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foundationId, userId);
    }
}