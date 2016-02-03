package edu.usm.config;


import edu.usm.domain.Role;
import edu.usm.domain.User;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
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

    @Autowired
    Environment env;

    @Value("${bayard.implementation.startup.email}")
    private String superuserEmail;
    @Value("${bayard.implementation.startup.firstName}")
    private String superuserFirstName;
    @Value("${bayard.implementation.startup.lastName}")
    private String superuserLastName;
    @Value("${bayard.implementation.startup.password}")
    private String superuserPassword;

    @Value("${bayard.dev.basicUser.email}")
    private String devEmail;
    @Value("${bayard.dev.basicUser.firstName}")
    private String devFirstName;
    @Value("${bayard.dev.basicUser.lastName}")
    private String devLastName;
    @Value("${bayard.dev.basicUser.password}")
    private String devPassword;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event){
        configureAuthentication();
        try {
            createUsers();
        } catch (ConstraintViolation e) {
            System.err.print("Error creating users on startup");
        }
        clearAuthentication();
    }

    private void createUsers() throws ConstraintViolation{
        createSuperuser();
        for (String profile: env.getActiveProfiles()) {
            if (profile.equals(BayardSpringProfiles.DEV_PROFILE)) {
                User user = userService.findByEmail(devEmail);
                if (user == null) {
                    user = new User();
                    user.setEmail(devEmail);
                    user.setRole(Role.ROLE_USER);
                    user.setFirstName(devFirstName);
                    user.setLastName(devLastName);
                    userService.createUser(user, devPassword);
                }
            }
        }
    }

    private void createSuperuser() throws ConstraintViolation{
        User superUser = userService.findByEmail(superuserEmail);
        if (superUser == null) {
            superUser = new User();
            superUser.setFirstName(superuserFirstName);
            superUser.setLastName(superuserLastName);
            superUser.setEmail(superuserEmail);
            superUser.setRole(Role.ROLE_SUPERUSER);
            userService.createUser(superUser, superuserPassword);
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


}