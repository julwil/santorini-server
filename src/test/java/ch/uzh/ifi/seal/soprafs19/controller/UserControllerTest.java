package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
public class UserControllerTest {



    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void fetchAllUsersWithInvalidToken() throws Exception {
        this.mvc.perform(get("/users").header("authorization", "fake_token")).andDo(print()).andExpect(status().is(401));
    }

    @Test
    public void fetchUserWithInvalidToken() throws Exception {
        this.mvc.perform(get("/users/1").header("authorization", "fake_token")).andDo(print()).andExpect(status().is(401));
    }

    @Test
    public void createUser() throws Exception {

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test User\",\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.path", notNullValue()));

        userRepository.delete(userRepository.findByUsername("testUser"));

    }

    @Test
    public void loginUser() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setName("Test User");
        testUser.setPassword("testPassword");
        String path = userService.postCreateUser(testUser);

        this.mvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.token", notNullValue()));

        userRepository.delete(userRepository.findByUsername("testUser"));

    }

    @Test
    public void logoutUser() throws Exception{
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setName("Test User");
        testUser.setPassword("testPassword");
        String path = userService.postCreateUser(testUser);
        String token = userService.postLogin(testUser);


        this.mvc.perform(get("/users/logout")
                .header("authorization", token))
                .andExpect(status().is(204));

        userRepository.delete(userRepository.findByUsername("testUser"));
    }

    @Test
    public void updateUser() throws Exception {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setName("Test User");
        testUser.setPassword("testPassword");
        String path = userService.postCreateUser(testUser);
        String token = userService.postLogin(testUser);
        long id = userRepository.findByUsername(testUser.getUsername()).getId();

        this.mvc.perform(put("/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", token)
                .content("{\"name\": \"Test User Updated\",\"username\": \"testUserUpdated\", \"password\": \"testPasswordUpdated\"}"))
                .andExpect(status().is(204)); //andDo(print()).
        userRepository.delete(userRepository.findByUsername("testUserUpdated"));

    }


    //package ch.uzh.ifi.seal.soprafs19.controller;
//
//import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
//import ch.uzh.ifi.seal.soprafs19.entity.User;
//import ch.uzh.ifi.seal.soprafs19.service.UserService;
//import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//import static org.hamcrest.core.Is.is;
//import static org.mockito.Mockito.*;
//
//import static org.springframework.http.MediaType.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@WebAppConfiguration
//@RunWith(MockitoJUnitRunner.class)
//public class UserControllerTest {
//
//    private MockMvc mockMvc;
//    private User testUser, dbUser;
//
//    @Mock
//    private UserService userService;
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks // instantiates a UserController which gets the Mocks injected
//    private UserController userController;
//
//    private final Long USER_ID = 1L;
//
//    @Before
//    public void setUp() throws Exception {
//        //MockitoAnnotations.initMocks(this); // do we need this?
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(userController)
//                .build();
//        // mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        testUser = new User();
//        testUser.setName("testName");
//        testUser.setUsername("testUsername");
//        testUser.setPassword("testPassword"); // needed for creation of new user
//        testUser.setBirthDate("2019-3-12"); // needed for creation of new user
//        testUser.setId(USER_ID);
//
//        dbUser = new User();
//        dbUser.setName("dbName");
//        dbUser.setUsername("dbUsername");
//        dbUser.setPassword("dbPassword"); // needed for creation of new user
//        dbUser.setBirthDate("2019-3-12"); // needed for creation of new user
//        dbUser.setId(USER_ID+1);
//
//        System.out.println("\n\n\n\n\n"+dbUser.getToken()+"\n\n\n\n");
//    }
//
//    @Test
//    public void all() throws Exception {
//
//        //Iterable<User> allUsers = singletonList(testUser);
//        List<User> allUsers = new ArrayList<>();
//        allUsers.add(testUser);
//        allUsers.add(dbUser);
//
//        when(userService.getUsers()).thenReturn(allUsers);
//        mockMvc.perform(get("/users")
//                .contentType(APPLICATION_JSON_UTF8_VALUE))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.*",hasSize(2)))
//                .andExpect(jsonPath("$[0].username", is(testUser.getUsername())))
//                .andExpect(jsonPath("$[0].name", is(testUser.getName())))
//                .andExpect(jsonPath("$[0].password", is(testUser.getPassword())))
//                .andExpect(jsonPath("$[0].birthDate", is("2019-3-12")))
//                .andExpect(jsonPath("$[0].id", is(1)));
//
//        verify(userService, times(1)).getUsers();
//        verifyNoMoreInteractions(userService);
//    }
//
////    @Test
////    public void getUser() throws Exception {
////        System.out.println("\n\n\nuserId: " + testUser.getId() + "\n\n\n");
////        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(testUser)); //ensure there is a user with id 1
////        Long userId = testUser.getId();
////
////        mockMvc.perform(get("/users/{userId}", userId)
////                .contentType(APPLICATION_JSON_UTF8_VALUE))
////                .andExpect(status().isOk())
////                .andDo(print())
////                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
////                .andExpect(jsonPath("$.name", is(testUser.getName())))
////                .andExpect(jsonPath("$.password", is(testUser.getPassword())))
////                .andExpect(jsonPath("$.birthDate", is("2019-3-12")));
////
////        verify(userRepository, times(1)).findById(userId);
////        verifyNoMoreInteractions(userRepository);
////    }
//
////    @Test // Test Registration
////    public void createUser() throws Exception {
////
////        String json = "{\n" +
////                "  \"username\": \"testUsername\",\n" +
////                "  \"password\": \"testPassword\",\n" +
////                "  \"name\": \"testName\",\n" +
////                "  \"birthDate\": \"2019-3-12\"\n" +
////                "}";
////
////        //when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(testUser)); //ensure there is a user with id 1
////        //when(userService.createUser(testUser)).thenReturn(any(User.class));
////        //when(userController.getUser(testUser.getId())).thenReturn(testUser);
////        mockMvc.perform(MockMvcRequestBuilders.post("/users")
////                .contentType(APPLICATION_JSON_VALUE)
////                .characterEncoding("utf-8")
////                .content(json))
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.*",hasSize(8)))
////                .andExpect(jsonPath("$.username", is("testUsername")))
////                .andExpect(jsonPath("$.name", is("testName")))
////                .andExpect(jsonPath("$.password", is("testPassword")))
////                .andExpect(jsonPath("$.birthDate", is("2019-3-12")));
////
////        verify(userService, times(1)).createUser(any(User.class));
////        verifyNoMoreInteractions(userService);
////    }
//
//    @Test
//    public void loginUser() throws Exception {
//        String json = "{\n" +
//                "  \"username\": \"testUsername\",\n" +
//                "  \"password\": \"testPassword\"\n" +
//                "}";
//        testUser.setStatus(UserStatus.ONLINE);
//        //when(userService.logInUser(testUser.getId(),testUser)).thenReturn(testUser);
//        //when(userController.getUser(testUser.getId())).thenReturn(testUser);
//        mockMvc.perform(MockMvcRequestBuilders.put("users/login/{userId}",1L)
//                .contentType(APPLICATION_JSON_UTF8_VALUE)
//                .content(json))
//                .andDo(print());
//                //.andExpect(status().isOk());
//                //.andExpect(jsonPath("$.status", is(UserStatus.ONLINE)));
//
//        //verify(userService, times(1)).logInUser(eq(testUser.getId()),any(User.class));
//        verifyNoMoreInteractions(userService);
//    }
//
//    @Test
//    public void logoutUser() throws Exception {
//
//    }
//
//    @Test
//    public void editUser() throws Exception {
//
//        String json = "{\n" +
//                "  \"username\": \"usernameTest\",\n" +
//                "  \"birthDate\": \"4321\"\n" +
//                "}";
//
//        // when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(testUser));
////        doNothing()
////                .doThrow(new RuntimeException())
////                .when(userService).editUser(testUser.getId(),testUser);
//        mockMvc.perform(
//                MockMvcRequestBuilders.put("/users/{userId}",1L)
//                    .contentType(APPLICATION_JSON)
//                    .characterEncoding("utf-8")
//                    .content(json))
//                    .andDo(print())
//                    .andExpect(status().isOk()); // successful 204
//
//
//        //verify(userRepository,times(0)).findById(testUser.getId());
//        verify(userService, times(1)).editUser(eq(testUser.getId()),any(User.class));
//        verifyNoMoreInteractions(userService);
//
//        //verify(userService, times(1)).updateUser(testUser,testUser.getId());
//        //verifyNoMoreInteractions(userService);
//
//
//    }
//}

}
