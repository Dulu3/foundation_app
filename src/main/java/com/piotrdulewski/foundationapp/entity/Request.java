package com.piotrdulewski.foundationapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
@Entity
@Table(name = "requests")
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RequestUser> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "request", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RequestAttachments> attachments;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private RequestCategory requestCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foundation_id")
    @JsonIgnore
    private Foundation foundation;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_time")
    private Timestamp creationTime;

    @Column(name = "points")
    private Integer points;

    @Column(name = "number_of_entries")
    private Integer numberOfEntries;

    @Column(name = "minimum_rating")
    private Short minimumRating;

    @Column(name = "time_to_reduce_rating")
    private String timeToReduceRating;

    @Column(name = "request_time")
    private Timestamp requestTime;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updateAt;

}