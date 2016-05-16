package edu.usm.service;

import edu.usm.domain.User;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.SecurityConstraintException;
import edu.usm.dto.NewUserDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by scottkimball on 7/17/15.
 */
public interface UserService {

    User findByEmail(String email);

    long createStartupUser(NewUserDto dto) throws SecurityConstraintException, ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    long createUser(User user, String password) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateUser(User user) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updateElevatedUser(User user) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void updateSuperUser(User user) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void updatePassword(User user, String currentPassword, String newPassword) throws ConstraintViolation, SecurityConstraintException;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void updateWithoutCurrentPassword(User user, String newPassword) throws ConstraintViolation, SecurityConstraintException;

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    long createAdministrativeUser(User user) throws ConstraintViolation;

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    List<User> getAllUsers();

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteUser(User user);

    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    User findById(long id);

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();


}
