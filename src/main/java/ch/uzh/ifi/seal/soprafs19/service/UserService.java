package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.exceptions.NotRegisteredException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.exceptions.UsernameAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.tomcat.jni.Local;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getAllUsers(String token) throws FailedAuthenticationException {
        if (validToken(token)) {
            return this.userRepository.findAll();
        } else {
            throw new FailedAuthenticationException();
        }
    }


    //Creates a new user if username not already taken and saves it to DB
    public User createUser(User newUser) throws UsernameAlreadyExistsException, JSONException {
        if(!userExists(newUser)) {
            newUser.setStatus(UserStatus.ONLINE);
            // use pseudo token to create user to retreieve id
            newUser.setToken("no_token");
            userRepository.save(newUser);
            generateTokenAndSaveEntity(newUser);
            return newUser;
        } else {
            throw new UsernameAlreadyExistsException();
        }
    }

    private void generateTokenAndSaveEntity(User newUser) throws JSONException {

        // Fetch the created user from db and extract userId and creation date
        newUser = userRepository.findByUsername(newUser.getUsername());
        long userId = newUser.getId();
        Date timeStamp = new Date();

        // Create Json with userId and creation date
       JSONObject json = new JSONObject();
       json.put("user_id", userId);
       json.put("token_created", timeStamp);

       // Convert json to String and encode b64
       String token = Base64.getEncoder().encodeToString(json.toString().getBytes());

       // Set token to user and update entity
        newUser.setToken(token);
        userRepository.save(newUser);
    }

    //Logs in a user if it userExists and validates if correct password is provided.
    public String login(User userToAuthenticate) throws FailedAuthenticationException, NotRegisteredException, JSONException {
        String username = userToAuthenticate.getUsername();
        String password = userToAuthenticate.getPassword();

        //Check if userToAuthenticate with that username exists in DB
        if(userExists(userToAuthenticate)) {
            User user = userRepository.findByUsername(userToAuthenticate.getUsername());
            //Validate password against DB. Upon success, return user
            if (password.equals(user.getPassword())){
                generateTokenAndSaveEntity(userToAuthenticate);
                userRepository.save(user);

                return user.getToken();

                //Raise 403 Forbiden exception.
            } else {
                throw new FailedAuthenticationException();
            }

            //Raise 404 User does not exist in DB. Throw not registered exception.
        } else {
            throw new NotRegisteredException();
        }
    }

    public User getUser(String token, long userId) throws FailedAuthenticationException, NotRegisteredException, NullPointerException {

        //Check if token exists
        if (userRepository.findByToken(token) != null) {

            return userRepository.findById(userId);
        }  else {
            throw new FailedAuthenticationException();
        }
    }

    // Check if a user with this username userExists in DB.
    public Boolean userExists(User user) {
        String username = user.getUsername();
        //Try to fetch user from DB with this username
        try {
            return userRepository.findByUsername(username) != null;
        } catch (NullPointerException e) {
            return false;
        }
    }

    // Check if token exists in DB
    public Boolean validToken(String tokenToAuthenticate) {
        try {
            return userRepository.findByToken(tokenToAuthenticate).getToken() != null;
        } catch (NullPointerException e) {
            return false;
        }
    }

    // Update a user
    public User updateUser(String originatorToken, long userToUpdateId, User userToUpdate) throws ResourceActionNotAllowedException, FailedAuthenticationException{
        // Check if token is valid
        if (userRepository.existsByToken(originatorToken)) {

            // Check if token from request-originator is related to the user-id from the user to be updated
            if (userRepository.findByToken(originatorToken).getId().equals(userToUpdateId)) {
                User dbUser = userRepository.findById(1);
                copyAttributes(dbUser, userToUpdate);
                userRepository.save(dbUser);
            } else {
                throw new ResourceActionNotAllowedException();
            }
        } else {
            throw new FailedAuthenticationException();
        }

        return null;
    }

    public void copyAttributes (User toUser, User fromUser){
        toUser.setUsername(fromUser.getUsername());
        toUser.setPassword(
                        fromUser.getPassword() != null ?
                        fromUser.getPassword() :  toUser.getPassword()
        );
        toUser.setName(fromUser.getName());
        toUser.setBirthday(fromUser.getBirthday());
    }
}
