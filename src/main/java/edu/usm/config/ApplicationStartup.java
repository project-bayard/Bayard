package edu.usm.config;


import edu.usm.domain.Role;
import edu.usm.domain.User;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

     @Autowired
    UserService userService;

    private String email = "superuser@email.com";
    private String firstName = "firstName";
    private String lastName = "lastName";
    private String password = "password";

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event){
        configureAuthentication();
        try {
            createSuperuser();
            createUser();
        } catch (ConstraintViolation e) {
            System.err.print("Error creating users on startup");
        }
        clearAuthentication();
    }

    private void createSuperuser() throws ConstraintViolation{
        User superUser = userService.findByEmail(email);
        if (superUser == null) {
            superUser = new User();
            superUser.setFirstName(firstName);
            superUser.setLastName(lastName);
            superUser.setEmail(email);
            superUser.setPasswordHash(password);
            superUser.setRole(Role.ROLE_SUPERUSER);
            userService.createUser(superUser);
       }
    }

    private void configureAuthentication () {
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList( "ROLE_SUPERUSER","ROLE_ELEVATED", "ROLE_USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "superuser",
                "superuser",
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void clearAuthentication () {
        SecurityContextHolder.clearContext();
    }

    private void createUser () throws ConstraintViolation{
        User user = userService.findByEmail("user@email.com");
        if (user == null) {
            user = new User();
            user.setEmail("user@email.com");
            user.setPasswordHash("password");
            user.setRole(Role.ROLE_USER);
            userService.createUser(user);
        }
    }

}