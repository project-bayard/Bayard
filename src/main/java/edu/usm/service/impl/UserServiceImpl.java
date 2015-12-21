package edu.usm.service.impl;

import edu.usm.domain.Role;
import edu.usm.domain.User;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.SecurityConstraintException;
import edu.usm.repository.UserDao;
import edu.usm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by scottkimball on 7/17/15.
 */

@Service
public class UserServiceImpl implements UserService {

    private static Integer minimumPasswordLength = 6;

    @Autowired
    UserDao userDao;

    @Override
    public User findByEmail(String email) {
        return userDao.findOneByEmail(email);
    }

    @Override
    public long createUser(User user, String password) throws ConstraintViolation {
        validatePassword(password);
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        return createUser(user);
    }

    private void validatePassword(String password) throws ConstraintViolation{
        if (null == password || password.isEmpty()) {
            throw new ConstraintViolation(ConstraintMessage.USER_NO_PASSWORD);
        }

        if (password.length() < minimumPasswordLength) {
            throw new ConstraintViolation(ConstraintMessage.USER_PASSWORD_TOO_SHORT);
        }
    }

    private void validateUniqueness(User user) throws ConstraintViolation {
        User existingEmail = findByEmail(user.getEmail());
        if (null != existingEmail && !existingEmail.getId().equals(user.getId())) {
            throw new ConstraintViolation(ConstraintMessage.USER_DUPLICATE_EMAIL);
        }
    }

    private long createUser(User user) throws ConstraintViolation {
        if (null == user.getPasswordHash()) {
            throw new ConstraintViolation(ConstraintMessage.USER_NO_PASSWORD);
        }
        if (user.getRole() == Role.ROLE_USER || user.getRole() == Role.ROLE_DEVELOPMENT) {
            validateUniqueness(user);
            userDao.save(user);
            return user.getId();
        } else {
            return createAdministrativeUser(user);
        }
    }

    @Override
    public void updateUser(User user) throws ConstraintViolation {
        validateUniqueness(user);
        if (user.getRole() == Role.ROLE_ELEVATED) {
            updateElevatedUser(user);
        } else if (user.getRole() == Role.ROLE_SUPERUSER) {
            updateSuperUser(user);
        } else {
            userDao.save(user);
        }
    }

    //Make use of interface's @PreAuthorize
    @Override
    public void updateElevatedUser(User user) throws ConstraintViolation {
        userDao.save(user);
    }

    //Make use of interface's @PreAuthorize
    @Override
    public void updateSuperUser(User user) throws ConstraintViolation {
        userDao.save(user);
    }

    @Override
    public void updatePassword(User user, String currentPassword, String newPassword) throws ConstraintViolation, SecurityConstraintException{

        boolean matches = new BCryptPasswordEncoder().matches(currentPassword, user.getPasswordHash());
        if (!matches) {
            throw new SecurityConstraintException("The current password does not match.");
        }
        validatePassword(newPassword);
        user.setPasswordHash(new BCryptPasswordEncoder().encode(newPassword));
        updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    @Override
    public User findById(long id) {
        return userDao.findOne(id);
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public long createAdministrativeUser(User user) throws ConstraintViolation{
        validateUniqueness(user);
        userDao.save(user);
        return user.getId();
    }
}
