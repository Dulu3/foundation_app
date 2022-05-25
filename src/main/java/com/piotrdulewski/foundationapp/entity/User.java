package com.piotrdulewski.foundationapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "benefit",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH, CascadeType.DETACH})
    @JsonIgnore
    private List<BenefitUser> benefits;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH, CascadeType.DETACH})
    @JsonIgnore
    private List<RequestUser> requests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH, CascadeType.DETACH})
    @JsonIgnore
    private List<Request> createdRequests;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "foundation")
    @JsonIgnore
    private List<FoundationUser> foundations;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "logo_path")
    private String logo_path;

    @Column(name = "email_verified_at")
    private Timestamp emailVerifiedAt;

    @Column(name = "password")
    private String password;

    @Column(name = "is_needy")
    private Byte isNeedy;

    @Column(name = "is_active")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isActive;

    @Column(name = "remember_token")
    private String rememberToken;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Override
    @ApiModelProperty(hidden = true)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> role1 = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        return role1;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.isActive;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
