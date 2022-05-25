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
@Table(name = "foundation_user")
@NoArgsConstructor
@Getter
@Setter
public class FoundationUser {

    @JsonIgnore
    @EmbeddedId
    private FoundationUserKey id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.DETACH})
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.DETACH})
    @MapsId("foundationId")
    @JoinColumn(name = "foundation_id")
    private Foundation foundation;

    @Column(name = "join_time")
    private Timestamp joinTime;
}
