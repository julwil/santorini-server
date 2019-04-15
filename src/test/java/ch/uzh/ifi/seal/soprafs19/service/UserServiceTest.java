package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceNotFoundException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.exceptions.UsernameAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Test class for the UserResource REST resource.
 * test commit
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest{


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Test
    public void createUserTest() throws JSONException, UsernameAlreadyExistsException {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        String path = userService.createUser(testUser);


        Assert.assertNotNull(testUser.getToken());
        Assert.assertEquals(UserStatus.OFFLINE, testUser.getStatus());
        Assert.assertEquals(testUser, userRepository.findByToken(testUser.getToken()));
        Assert.assertNotNull(userRepository.findByUsername(testUser.getUsername()).getPassword());

        userRepository.delete(testUser);
    }


    @Test
    public void loginSuccessfulTest() throws ResourceNotFoundException, JSONException, FailedAuthenticationException, UsernameAlreadyExistsException {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        String path = userService.createUser(testUser);

        String token = userService.login(testUser);

        // Check if the token returned matches the to authenticated user
        Assert.assertEquals(token, testUser.getToken(), userRepository.findByUsername(testUser.getUsername()).getToken());

        // Check if user is online
        Assert.assertEquals(UserStatus.ONLINE, userRepository.findByToken(token).getStatus());
        userRepository.delete(testUser);
    }

    @Test
    public void logoutTest() throws ResourceNotFoundException, UsernameAlreadyExistsException, FailedAuthenticationException {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        String path = userService.createUser(testUser);

        // Check if the token returned matches the to authenticated user
        String token = userService.login(testUser);
        User userToLogout = userRepository.findByToken(token);
        userService.logout(testUser.getToken());
        User loggedOutUser = userRepository.findByUsername(userToLogout.getUsername());
        // Check if user status is offline
        Assert.assertEquals(UserStatus.OFFLINE, loggedOutUser.getStatus());

        // Check if token is invalidated
        Assert.assertFalse(token.equals(userRepository.findById(userToLogout.getId())));

        userRepository.delete(testUser);
    }


    @Test
    public void loginFailedWrongPasswordTest() throws ResourceNotFoundException, JSONException, FailedAuthenticationException, UsernameAlreadyExistsException {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        String path = userService.createUser(testUser);

        String token = null;
        try {
            testUser.setPassword("hello");
            token = userService.login(testUser);
        }
        catch (FailedAuthenticationException e) {
            Assert.assertEquals("Failed Authentication. Check your username and password", e.getMessage());
        } finally {
            Assert.assertNull(token);
            userRepository.delete(testUser);
        }
    }

    @Test
    public void loginFailedNonExistentUserNameTest() throws ResourceNotFoundException, JSONException, FailedAuthenticationException, UsernameAlreadyExistsException {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        String path = userService.createUser(testUser);

        String token = null;
        try {
            testUser.setUsername("hello");
            token = userService.login(testUser);
        }
        catch (ResourceNotFoundException e) {
            Assert.assertEquals("User does not exist", e.getMessage());
        } finally {
            Assert.assertNull(token);
            userRepository.delete(testUser);
        }
    }

    @Test
    public void updateUserTest() throws UsernameAlreadyExistsException, ResourceActionNotAllowedException, FailedAuthenticationException {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        String path = userService.createUser(testUser);

        testUser.setUsername("myNewUsername");
        userService.updateUser(testUser.getToken(), testUser.getId(), testUser);

        Assert.assertEquals(testUser, userRepository.findByUsername("myNewUsername"));
    }
}
