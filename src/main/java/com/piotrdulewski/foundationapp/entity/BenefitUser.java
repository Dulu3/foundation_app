package com.piotrdulewski.foundationapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@ApiModel
@Entity
@Table(name = "benefit_user")
@NoArgsConstructor
@Getter
@Setter
public class BenefitUser {

    @JsonIgnore
    @EmbeddedId
    private BenefitUserKey embeddedId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.DETACH})
    @MapsId("benefitId")
    @JoinColumn(name = "benefit_id")
    private Benefit benefit;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.DETACH})
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "redemption_time")
    private Timestamp redemption_time;

}