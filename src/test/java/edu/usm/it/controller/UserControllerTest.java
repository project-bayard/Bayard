package edu.usm.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.Role;
import edu.usm.domain.User;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.dto.Response;
import edu.usm.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Base64;
import java.util.ConcurrentModificationException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by scottkimball on 7/26/15.
 */
public class UserControllerTest extends WebAppConfigurationAware {

    private static final String FIRST_NAME = "first";
    private static final String LAST_NAME = "last";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private UserService userService;

    private User user;
    private long userID;

    @Before
    public void setup() throws ConstraintViolation{
        user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);
        user.setPasswordHash(PASSWORD);
        user.setRole(Role.ROLE_USER);
        userID = userService.createUser(user);
    }

    @After
    public void teardown() {
        userService.deleteAll();
    }

    @Test
    public void testAuthorize () throws Exception {

        String auth =  EMAIL + ":" + PASSWORD;
        byte[] bytesEncoded = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String( bytesEncoded );

        mockMvc.perform(get("/users/authenticate").accept(MediaType.APPLICATION_JSON).header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.role", is("ROLE_USER")))
                .andExpect(jsonPath("$.password", is(nullValue())));
    }

    @Test
    public void testGetUser() throws Exception {
        mockMvc.perform(get("/users/" + userID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.role", is("ROLE_USER")))
                .andExpect(jsonPath("$.password", is(nullValue())));
    }

    @Test
    public void testCreateUser () throws Exception {
        String email = "newemail@email.com";
        User newUser = new User();
        newUser.setFirstName(LAST_NAME);
        newUser.setLastName(FIRST_NAME);
        newUser.setRole(Role.ROLE_SUPERUSER);
        newUser.setPasswordHash(PASSWORD);
        newUser.setEmail(email);

        String body = new ObjectMapper().writeValueAsString(newUser);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        User fromDb = userService.findByEmail(email);
        assertNotNull(fromDb);
        assertEquals(fromDb.getFirstName(), newUser.getFirstName());
        assertEquals(fromDb.getLastName(), newUser.getLastName());
        assertEquals(fromDb.getRole(), newUser.getRole());
        assertEquals(fromDb.getEmail(), newUser.getEmail());
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/" + userID))
                .andExpect(status().isOk());

        User fromDb = userService.findById(userID);
        assertNull(fromDb);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
