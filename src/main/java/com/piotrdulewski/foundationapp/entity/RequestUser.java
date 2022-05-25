package com.piotrdulewski.foundationapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@ApiModel
@Entity
@Table(name = "request_user")
@Getter
@Setter
public class RequestUser {

    @JsonIgnore
    @EmbeddedId
    private RequestUserKey requestUserKey;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.DETACH})
    @MapsId("requestId")
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH, CascadeType.DETACH})
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "points")
    private Integer points;

    @Column(name = "verified")
    private Byte verified;

    @Column(name = "completion_time")
    private Timestamp completionTime;

}