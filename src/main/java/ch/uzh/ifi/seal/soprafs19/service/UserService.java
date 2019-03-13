package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.exceptions.NotRegisteredException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.exceptions.UsernameAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // Creates a new user if username not already taken and saves it to DB. Returns a path to the created user (e.g. users/5)
    public String createUser(User newUser) throws UsernameAlreadyExistsException {
        if (!userRepository.existsByUsername(newUser.getUsername())) {
            newUser.setStatus(UserStatus.ONLINE);

            // use pseudo token to create user in order to generate a valid id
            newUser.setToken("no_token");
            userRepository.save(newUser);

            // generate a token with the valid id
            generateTokenAndSaveEntity(newUser);
            return "/users/"+newUser.getId().toString();
        } else {
            throw new UsernameAlreadyExistsException();
        }
    }

    // Update a user
    public User updateUser(String originatorToken, long userToUpdateId, User userToUpdate) throws ResourceActionNotAllowedException, FailedAuthenticationException, UsernameAlreadyExistsException {
        // Check if token is valid
        if (userRepository.existsByToken(originatorToken)) {

            // Check if token from request-originator is related to the user-id from the user to be updated
            if (userRepository.findByToken(originatorToken).getId().equals(userToUpdateId)) {
                User dbUser = userRepository.findById(userToUpdateId);

                if (dbUser.getUsername().equals(userToUpdate.getUsername()) ||
                    !userRepository.existsByUsername(userToUpdate.getUsername())) {
                    copyAttributes(dbUser, userToUpdate);
                    userRepository.save(dbUser);
                } else {
                    throw new UsernameAlreadyExistsException();
                }

            } else {
                throw new ResourceActionNotAllowedException();
            }
        } else {
            throw new FailedAuthenticationException();
        }

        return null;
    }

    //Logs in a user if the user exists and password is correct. Returns the users's token
    public String login(User userToAuthenticate) throws FailedAuthenticationException, NotRegisteredException{
        String username = userToAuthenticate.getUsername();
        String password = userToAuthenticate.getPassword();

        //Check if userToAuthenticate with that username exists in DB
        if(userRepository.existsByUsername(userToAuthenticate.getUsername())) {
            User user = userRepository.findByUsername(userToAuthenticate.getUsername());

            //Validate password against DB. Upon success, return the new user-token
            if (password.equals(user.getPassword())){
                generateTokenAndSaveEntity(userToAuthenticate);
                userRepository.save(user);

                return user.getToken();
            } else {
                throw new FailedAuthenticationException();
            }
        } else {
            throw new NotRegisteredException();
        }
    }

    // Fetch all users
    public Iterable<User> getAllUsers(String token) throws FailedAuthenticationException {
        if (userRepository.existsByToken(token)) {
            return this.userRepository.findAll();
        } else {
            throw new FailedAuthenticationException();
        }
    }

    // Get one particular user. A valid token and a user id must be provided
    public User getUser(String token, long userId) throws FailedAuthenticationException, NotRegisteredException, NullPointerException {

        //Check if token exists
        if (userRepository.findByToken(token) != null) {

            return userRepository.findById(userId);
        }  else {
            throw new FailedAuthenticationException();
        }
    }

    // Create a token for the user
    private void generateTokenAndSaveEntity(User newUser) {

        // Fetch the created user from db and extract userId and creation date
        newUser = userRepository.findByUsername(newUser.getUsername());
        long userId = newUser.getId();
        Date timeStamp = new Date();

        // Create JSON with userId and creation date
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("token_created", "Wtf");



       JSONObject json = new JSONObject();
       json.put("user_id", userId);
       json.put("token_created", timeStamp);

       // Convert json to String and encode it with b64
       String token = Base64.getEncoder().encodeToString(json.toString().getBytes());

       // Set token to user and update entity
        newUser.setToken(token);
        userRepository.save(newUser);
    }

    public void copyAttributes (User toUser, User fromUser) {
        toUser.setUsername(
                fromUser.getUsername() != null ?
                fromUser.getUsername() : toUser.getUsername()
        );
        toUser.setPassword(
            fromUser.getPassword() != null ?
            fromUser.getPassword() :  toUser.getPassword()
        );
        toUser.setName(fromUser.getName());
        toUser.setBirthday(fromUser.getBirthday());
    }
}
