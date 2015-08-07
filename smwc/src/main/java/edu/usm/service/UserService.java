package edu.usm.service;

import edu.usm.domain.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by scottkimball on 7/17/15.
 */
public interface UserService {

    User findByEmail(String email);

    @PreAuthorize(value = "hasRole('ROLE_ELEVATED')")
    long createUser(User user);

    @PreAuthorize(value = "hasRole('ROLE_SUPERUSER')")
    long createAdministrativeUser(User user);

    @PreAuthorize(value = "hasRole('ROLE_ELEVATED')")
    List<User> getAllUsers();

    @PreAuthorize(value = "hasRole('ROLE_SUPERUSER')")
    void deleteUser(User user);

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    User findById(long id);

    @PreAuthorize(value = "hasRole('ROLE_SUPERUSER')")
    void deleteAll();


}
