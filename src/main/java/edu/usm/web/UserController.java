package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.User;
import edu.usm.domain.Views;
import edu.usm.dto.Response;
import edu.usm.service.UserService;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by scottkimball on 7/18/15.
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Response createUser(@RequestBody User user) {
        try {
            long id = userService.createUser(user);
            return new Response(Long.toString(id),Response.SUCCESS);
        } catch (Exception e) {
            return new Response(null, "Failed to add new user");
        }
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.UserDetails.class)
    public User authorizeUser (@RequestHeader("Authorization") String auth) {
        String base64Credentials = auth.substring("Basic".length()).trim();
        String credentials = new String(Base64.decode(base64Credentials),
                Charset.forName("UTF-8"));
        final String[] values = credentials.split(":",2);
        return userService.findByEmail(values[0]);
    }

    @RequestMapping(value = "/{userId}" , method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.UserDetails.class)
    public User getUser(@PathVariable("userId") long userId) {
        return userService.findById(userId);
    }

    @RequestMapping(value = "/{userId}" , method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public Response deleteUser(@PathVariable("userId") long userId) {
        User user = userService.findById(userId);
        if (null == user) {
            return new Response(null, "User with ID " + userId + " does not exist.");
        }
        try {
            userService.deleteUser(user);
            return Response.successGeneric();
        } catch (Exception e) {
            return new Response(null, "Error deleting user with ID " + userId + ".");
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.UserDetails.class)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
