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

    @Autowired
    UserDao userDao;

    @Override
    public User findByEmail(String email) {
        return userDao.findOneByEmail(email);
    }

    @Override
    public long createUser(User user, String password) throws ConstraintViolation {
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        return createUser(user);
    }

    @Override
    public long createUser(User user) throws ConstraintViolation{
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
        userDao.save(user);
    }

    @Override
    public void updatePassword(User user, String currentPassword, String newPassword) throws ConstraintViolation, SecurityConstraintException{

        boolean matches = new BCryptPasswordEncoder().matches(user.getPasswordHash(), currentPassword);
        if (!matches) {
            throw new SecurityConstraintException("The current password does not match");
        }

        user.setPasswordHash(new BCryptPasswordEncoder().encode(newPassword));
        updateUser(user);
    }

    private void validateUniqueness(User user) throws ConstraintViolation {
        User existingEmail = findByEmail(user.getEmail());
        if (null != existingEmail && !existingEmail.getId().equals(user.getId())) {
            throw new ConstraintViolation(ConstraintMessage.USER_DUPLICATE_EMAIL);
        }
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
    public long createAdministrativeUser(User user) {
        userDao.save(user);
        return user.getId();
    }
}
