package edu.usm.service.impl;

import edu.usm.domain.User;
import edu.usm.domain.UserWithDetails;
import edu.usm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by scottkimball on 7/18/15.
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findByEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException("User with email " + s + " does not exist");
        } else {
            return new UserWithDetails(user);
        }
    }
}
