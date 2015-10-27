package edu.usm.service.impl;

import edu.usm.domain.Role;
import edu.usm.domain.User;
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
    public long createUser(User user) {
        if (user.getRole() == Role.ROLE_USER || user.getRole() == Role.ROLE_DEVELOPMENT) {
            String password = user.getPasswordHash();
            user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
            userDao.save(user);
            return user.getId();
        } else {
            return createAdministrativeUser(user);
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
        String password = user.getPasswordHash();
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        userDao.save(user);
        return user.getId();
    }
}
