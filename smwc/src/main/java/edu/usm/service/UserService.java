package edu.usm.service;

import edu.usm.domain.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by scottkimball on 7/17/15.
 */
public interface UserService {

    User findByEmail(String email);

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    long createUser(User user);

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    long createAdministrativeUser(User user);

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    List<User> getAllUsers();

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteUser(User user);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    User findById(long id);

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();


}
