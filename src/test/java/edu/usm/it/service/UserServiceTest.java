package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Role;
import edu.usm.domain.User;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by andrew on 11/3/15.
 */
public class UserServiceTest extends WebAppConfigurationAware {

    @Autowired
    UserService userService;

    private User testUser;
    private String password;

    @Before
    public void setup() {
        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@email.com");
        password = "password";
    }

    @After
    public void teardown() {
        userService.deleteAll();
    }

    @Test
    public void testCreateUser() throws Exception{
        testUser.setRole(Role.ROLE_USER);
        userService.createUser(testUser, password);

        User fromDb = userService.findByEmail(testUser.getEmail());
        assertNotNull(fromDb);
        assertEquals(testUser.getRole(), fromDb.getRole());
    }

    @Test
    public void testCreateSuperUser() throws Exception{
        testUser.setRole(Role.ROLE_SUPERUSER);
        userService.createUser(testUser, password);

        User fromDb = userService.findByEmail(testUser.getEmail());
        assertNotNull(fromDb);
        assertEquals(testUser.getRole(), fromDb.getRole());
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateUserDuplicateEmail() throws Exception{
        testUser.setRole(Role.ROLE_USER);
        userService.createUser(testUser, password);

        User newUser = new User();
        newUser.setFirstName("New");
        newUser.setLastName("User");
        newUser.setEmail(testUser.getEmail());
        newUser.setRole(Role.ROLE_USER);
        userService.createUser(newUser, "a password");
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateSuperUserDuplicateEmail() throws Exception{
        testUser.setRole(Role.ROLE_SUPERUSER);
        userService.createUser(testUser, password);

        User newUser = new User();
        newUser.setFirstName("New");
        newUser.setLastName("User");
        newUser.setEmail(testUser.getEmail());
        newUser.setRole(Role.ROLE_SUPERUSER);
        userService.createUser(newUser, "a password");
    }

}
