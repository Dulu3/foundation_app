package com.piotrdulewski.foundationapp.entity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.MetaValue;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;

@ApiModel
@Entity
@Table(name = "mail_verification_token")
@Getter
@Setter
public class MailVerificationToken {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Any(metaColumn = @Column(name = "verification_type"))
    @AnyMetaDef(idType = "long", metaType = "string",
            metaValues = {
                    @MetaValue(targetEntity = User.class, value = "U"),
                    @MetaValue(targetEntity = Foundation.class, value = "F")
            })
    @Cascade(org.hibernate.annotations.CascadeType.REFRESH)
    @JoinColumn(name = "verification_id")
    private UserDetails userDetails;

    @Column(name = "token")
    private String token;

    @Column(name = "expire_at")
    private Timestamp expire_at;

    @Column(name = "created_at")
    private Timestamp created_at;

}