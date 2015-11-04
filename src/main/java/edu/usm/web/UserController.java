package edu.usm.web;

import com.fasterxml.jackson.annotation.JsonView;
import edu.usm.domain.User;
import edu.usm.domain.Views;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.domain.exception.InvalidApiRequestException;
import edu.usm.domain.exception.SecurityConstraintException;
import edu.usm.dto.NewUserDto;
import edu.usm.dto.Response;
import edu.usm.dto.PasswordChangeDto;
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
    public Response createUser(@RequestBody NewUserDto dto) throws ConstraintViolation {
        User newUser = new User();
        newUser.setFirstName(dto.getFirstName());
        newUser.setLastName(dto.getLastName());
        newUser.setEmail(dto.getEmail());
        newUser.setRole(dto.getRole());
        long id = userService.createUser(newUser, dto.getPassword());
        return new Response(Long.toString(id),Response.SUCCESS);
    }

    @RequestMapping(value="/{userId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Response updateUser(@PathVariable("userId") long userId, @RequestBody User user) throws ConstraintViolation, InvalidApiRequestException {
        User fromDb = userService.findById(userId);
        if (null == user) {
            throw new InvalidApiRequestException("User with ID " + userId + " does not exist.");
        }
        fromDb.setFirstName(user.getFirstName());
        fromDb.setLastName(user.getLastName());
        fromDb.setEmail(user.getEmail());
        userService.updateUser(fromDb);
        return Response.successGeneric();
    }

    @RequestMapping(value="/{userId}/password", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public Response updatePassword(@PathVariable("userId") long userId, @RequestBody PasswordChangeDto dto) throws ConstraintViolation, SecurityConstraintException, InvalidApiRequestException {
        User user = userService.findById(userId);
        if (null == user) {
            throw new InvalidApiRequestException("User with ID " + userId + " does not exist.");
        }
        userService.updatePassword(user, dto.getCurrentPassword(), dto.getNewPassword());
        return Response.successGeneric();
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
    public Response deleteUser(@PathVariable("userId") long userId) throws InvalidApiRequestException{
        User user = userService.findById(userId);
        if (null == user) {
            throw new InvalidApiRequestException("User with ID " + userId + " does not exist.");
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
