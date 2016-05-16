package edu.usm.config;


import edu.usm.domain.DemographicCategory;
import edu.usm.domain.Role;
import edu.usm.domain.User;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.service.ConfigService;
import edu.usm.service.DemographicCategoryService;
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

import java.util.*;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    UserService userService;

    @Autowired
    DemographicCategoryService demographicCategoryService;

    @Autowired
    ConfigService configService;

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

    /**
     * On startup, creates baseline demographic categories, persists an initial implementation config iff none already
     * exists, and, unless in production environment, creates baseline users
     * @param event
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event){
        configureAuthentication();
        Set<String> profiles = new HashSet<>(Arrays.asList(env.getActiveProfiles()));
        try {
            createBaselineDemographicCategories();
            configService.persistStartupConfig();
            if (!profiles.contains(BayardSpringProfiles.PRODUCTION_PROFILE) && profiles.contains(BayardSpringProfiles.DEV_PROFILE)) {
                createUsers();
            }
        } catch (ConstraintViolation e) {
            System.err.print("Error on startup");
        }
        clearAuthentication();
    }

    /**
     * Creates startup DemographicCategories if they do not already exist
     * @throws ConstraintViolation
     */
    private void createBaselineDemographicCategories() throws ConstraintViolation{
        String[] startupDemographicCategories = configService.getStartupDemographicCategories();
        for (String category: startupDemographicCategories) {
            DemographicCategory cat = new DemographicCategory(category);
            try {
                demographicCategoryService.create(cat);
            } catch (ConstraintViolation e) {
                continue;
            }
        }
    }

    /**
     * Creates a superuser and, if in BayardSpringProfiles.DEV_PROFILE, a baseline dev user
     * @throws ConstraintViolation
     */
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