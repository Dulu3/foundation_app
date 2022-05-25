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
public class BenefitUserKey implements Serializable {

    @Column(name = "benefit_id")
    private Long benefitId;

    @Column(name = "user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BenefitUserKey)) return false;
        BenefitUserKey that = (BenefitUserKey) o;
        return getBenefitId().equals(that.getBenefitId()) && getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBenefitId(), getUserId());
    }
}