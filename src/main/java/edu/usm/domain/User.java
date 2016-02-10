package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A user of Bayard.
 */
@Entity(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue
    @JsonView(Views.UserDetails.class)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonView(Views.UserDetails.class)
    @NotNull
    private String email;

    @Column(nullable = false)
    @NotNull
    private String passwordHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(Views.UserDetails.class)
    @NotNull
    private Role role;

    @Column
    @JsonView(Views.UserDetails.class)
    @NotNull
    private String firstName;

    @Column
    @JsonView(Views.UserDetails.class)
    @NotNull
    private String lastName;

    public User() {
        super();
    }

    /**
     * @param id the id of th user
     * @param email the user's email
     * @param passwordHash the hash of the user's password
     * @param role the user's Role
     * @param firstName the user's first name
     * @param lastName the user's last name
     */
    public User(Long id, String email, String passwordHash, Role role, String firstName, String lastName) {
        super();
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

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