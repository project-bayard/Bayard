package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by scottkimball on 7/17/15.
 */
@Entity(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue
    @JsonView(Views.UserDetails.class)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonView(Views.UserDetails.class)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(Views.UserDetails.class)
    private Role role;

    @Column
    @JsonView(Views.UserDetails.class)
    private String firstName;

    @Column
    @JsonView(Views.UserDetails.class)
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}