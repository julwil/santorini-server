package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.utilities.Authentication;
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
            newUser.setStatus(UserStatus.OFFLINE);

            // Use pseudo token to create user in order to get a valid id
            newUser.setToken("no_token");
            userRepository.save(newUser);

            // Generate a token for the new user
            newUser.setToken(createUserToken(newUser));
            userRepository.save(newUser);

            return "/users/" + newUser.getId().toString();
        } else {
            throw new UsernameAlreadyExistsException();
        }
    }

    // Update a user
    public User updateUser(String originatorToken, long userToUpdateId, User userToUpdate) throws ResourceActionNotAllowedException, FailedAuthenticationException, UsernameAlreadyExistsException {
        // Check if token is valid
        Authentication authentication = new Authentication(userRepository);
        return authentication.updateUser(originatorToken,userToUpdateId,userToUpdate);

    }

    //Logs in a user if the user exists and password is correct. Returns the users's token
    public String login(User userToAuthenticate) throws FailedAuthenticationException, NotRegisteredException{
        String username = userToAuthenticate.getUsername();
        String password = userToAuthenticate.getPassword();

        //Check if userToAuthenticate with that username exists in DB
        if(userRepository.existsByUsername(userToAuthenticate.getUsername())) {
            User dbUser = userRepository.findByUsername(username);

            //Validate password against DB. Upon success, return the new user-token and
            if (password.equals(dbUser.getPassword())){
                dbUser.setToken(createUserToken(dbUser));
                dbUser.setStatus(UserStatus.ONLINE);
                userRepository.save(dbUser);

                return dbUser.getToken();
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
    private String createUserToken(User user) {

       // Fetch the user from db and extract userId and creation date
       user = userRepository.findByUsername(user.getUsername());
       long userId = user.getId();
       Date timeStamp = new Date();
       JSONObject json = new JSONObject();
       json.put("user_id", userId);
       json.put("token_created", timeStamp);

       // Convert json to String and encode it with b64
       return Base64.getEncoder().encodeToString(json.toString().getBytes());
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

    public boolean isOnline(User user) { return user.getStatus() == UserStatus.ONLINE; }
    public boolean isPlaying(User user) {
        return user.getStatus() == UserStatus.PLAYING;
    }
    public boolean isChallenged(User user) { return user.getStatus() == UserStatus.CHALLENGED; }

    public void logout(String userToLogoutToken) throws NotRegisteredException {
        try {

            User userToLogout = userRepository.findByToken(userToLogoutToken);
            userToLogout.setToken("logged_out");
            userToLogout.setStatus(UserStatus.OFFLINE);

            userRepository.save(userToLogout);
        }
        catch (NullPointerException e) {
            throw new NotRegisteredException();
        }
    }
}
