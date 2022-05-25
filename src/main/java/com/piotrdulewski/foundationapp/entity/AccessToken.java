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
@Table(name = "access_tokens")
@Getter
@Setter
public class AccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @Any(metaColumn = @Column(name = "tokenable_type"))
    @AnyMetaDef(idType = "long", metaType = "string",
            metaValues = {
                    @MetaValue(targetEntity = User.class, value = "user"),
                    @MetaValue(targetEntity = Foundation.class, value = "foundation")
            })
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "tokenable_id")
    private UserDetails userDetails;

    @Column(name = "last_used_at")
    private Timestamp lastUsedAt;

    @Column(name = "created_at")
    private Timestamp cratedAt;

    @Column(name = "expire_at")
    private Timestamp expiryAt;
}
