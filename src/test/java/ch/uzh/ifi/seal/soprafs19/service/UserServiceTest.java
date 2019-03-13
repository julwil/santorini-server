package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.exceptions.NotRegisteredException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.exceptions.UsernameAlreadyExistsException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.security.auth.login.FailedLoginException;

/**
 * Test class for the UserResource REST resource.
 *
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
        Assert.assertEquals(testUser.getStatus(),UserStatus.ONLINE);
        Assert.assertEquals(testUser, userRepository.findByToken(testUser.getToken()));
        Assert.assertNotNull(userRepository.findByUsername(testUser.getUsername()).getPassword());

        userRepository.delete(testUser);
    }


    @Test
    public void loginSuccessfulTest() throws NotRegisteredException, JSONException, FailedAuthenticationException, UsernameAlreadyExistsException {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        String path = userService.createUser(testUser);

        // Check if the token returned matches the to authenticated user
        String token = userService.login(testUser);
        Assert.assertEquals(token, testUser.getToken(), userRepository.findByUsername(testUser.getUsername()).getToken());

        userRepository.delete(testUser);
    }


    @Test
    public void loginFailedWrongPasswordTest() throws NotRegisteredException, JSONException, FailedAuthenticationException, UsernameAlreadyExistsException {
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
    public void loginFailedNonExistentUserNameTest() throws NotRegisteredException, JSONException, FailedAuthenticationException, UsernameAlreadyExistsException {
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
        catch (NotRegisteredException e) {
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
