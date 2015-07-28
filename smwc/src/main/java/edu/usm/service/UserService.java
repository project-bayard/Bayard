package edu.usm.service;

import edu.usm.domain.User;

import java.util.List;

/**
 * Created by scottkimball on 7/17/15.
 */
public interface UserService {

    User findByEmail(String email);
    long createUser(User user);
    List<User> getAllUsers();
    void deleteUser(User user);
    User findById(long id);
    void deleteAll();
}
