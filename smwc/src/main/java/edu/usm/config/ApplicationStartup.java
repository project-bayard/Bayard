package edu.usm.config;


import edu.usm.domain.Role;
import edu.usm.domain.User;
import edu.usm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private static final String EMAIL = "superuser@email.com";
    private static final String PASSWORD = "password";

     @Autowired
    UserService userService;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        User user = userService.findByEmail(EMAIL);
        if (user == null) {
            user = new User();
            user.setFirstName("first");
            user.setLastName("last");
            user.setEmail(EMAIL);
            user.setPasswordHash(PASSWORD);
            user.setRole(Role.SUPERUSER);
            userService.createUser(user);
        }
    }

}