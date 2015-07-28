package edu.usm.domain;

import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Created by scottkimball on 7/18/15.
 */
public class UserWithDetails extends org.springframework.security.core.userdetails.User  {

    private User user;

    public UserWithDetails(User user) {
        super(user.getEmail(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }
}
