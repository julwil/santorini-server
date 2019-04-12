package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.exceptions.NotRegisteredException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.exceptions.UsernameAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    // Create new user
    @PostMapping("/users")
    public Map<String, String> createUser(@Valid @RequestBody User newUser, HttpServletResponse response) throws UsernameAlreadyExistsException, JSONException {
        HashMap<String, String> pathToUser = new HashMap<>();
        pathToUser.put("path", this.service.createUser(newUser));

        // Upon success return the path to the created usr
        response.setStatus(201);
        return pathToUser;
    }

    // Login an existing user
    @PostMapping("/login")
    public Map<String, String> token (@RequestBody User userToAuthenticate) throws NotRegisteredException, FailedAuthenticationException, JSONException {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", this.service.login(userToAuthenticate));

        return map;
    }

    // Logout user
    @GetMapping("/logout")
    public void logout (@RequestHeader("authorization") String token) throws NotRegisteredException {
        this.service.logout(token);
    }

    // Fetch all users
    @GetMapping("/users") //users
    Iterable<User> allUsers (@RequestHeader("authorization") String token) throws FailedAuthenticationException {
        return service.getAllUsers(token);
    }

    // Fetch one particular user
    @GetMapping("/users/{userId}") //users
    User user (@RequestHeader("authorization") String token, @PathVariable(value="userId") long userId) throws NotRegisteredException, FailedAuthenticationException {
        return service.getUser(token, userId);
    }

    // Update one particular user
    @PutMapping("/users/{userId}") //users
    User user (@RequestHeader("authorization") String token, @PathVariable(value="userId") long userId, @RequestBody User userToUpdate) throws NotRegisteredException,
            FailedAuthenticationException, ResourceActionNotAllowedException, UsernameAlreadyExistsException {
        return service.updateUser(token, userId, userToUpdate);
    }
}
