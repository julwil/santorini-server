package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.After;
import org.junit.Before;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @Before
    public void init() throws Exception {
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setName("Test User");
        testUser.setPassword("testPassword");
        userService.createUser(testUser);
    }

    @After
    public void destruct(){
        userRepository.delete(testUser);
    }

//    @Test
//    public void postGame() throws Exception {
//        this.mvc.perform(post("/games")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"user1\": 1, \"playerId2\": 2, \"isGodPower\": false, \"status\": \"RUNNING\"}")
//                .header("Authorization", testUser.getToken()))
//                .andExpect(status().is(201))
//                .andExpect(header().string("Content-Type","application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.path").isString());
//    }

//    @Test
//    public void getGame() throws Exception {
//        this.mvc.perform(get("/games/1")
//                .header("Authorization", testUser.getToken()))
//                .andExpect(status().is(200))
//                .andExpect(header().string("Content-Type","application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.name").isString())
//                .andExpect(jsonPath("$.status").isString())
//                .andExpect(jsonPath("$.isGodPower").isBoolean())
//                .andExpect(jsonPath("$.createTime").isNotEmpty())
//                .andExpect(jsonPath("$.board").isNotEmpty())
//                .andExpect(jsonPath("$.currentTurn").isNotEmpty())
//                .andExpect(jsonPath("$.currentTurn.id").isNumber())
//                .andExpect(jsonPath("$.currentTurn.name").isString())
//                .andExpect(jsonPath("$.currentTurn.figures", notNullValue()));
//    }

    @Test
    public void getTurns() throws Exception {
        this.mvc.perform(get("/games/1/turns")
                .header("Authorization", testUser.getToken()))
                .andExpect(status().is(200))
                .andExpect(header().string("Content-Type","application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.turns").isNotEmpty())
                .andExpect(jsonPath("$.turns[0]").isNotEmpty())
                .andExpect(jsonPath("$.turns[0].id").isNumber())
                .andExpect(jsonPath("$.turns[0].createTime").isString())
                .andExpect(jsonPath("$.turns[0].performedBy").isNotEmpty())
                .andExpect(jsonPath("$.turns[0].performedBy.id").isNumber())
                .andExpect(jsonPath("$.turns[0].performedBy.name").isString())
                .andExpect(jsonPath("$.turns[0].performedBy.figures", notNullValue()))
                .andExpect(jsonPath("$.turns[0].finished").isBoolean())
                .andExpect(jsonPath("$.turns[0].events", notNullValue()));
    }

    @Test
    public void postTurn() throws Exception {
        this.mvc.perform(post("/games/1/turns")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"performedBy\": 1,\"finished\": true, \"events\": [\"event1\",\"event2\"]}")
                .header("Authorization", testUser.getToken()))
                .andExpect(status().is(201))
                .andExpect(header().string("Content-Type","application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.path").isString());
    }

    @Test
    public void getPlayers() throws Exception {
        this.mvc.perform(get("/games/1/players")
                .header("Authorization", testUser.getToken()))
                .andExpect(status().is(200))
                .andExpect(header().string("Content-Type","application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.players").isNotEmpty())
                .andExpect(jsonPath("$.players[0]").isNotEmpty())
                .andExpect(jsonPath("$.players[0].id").isNumber())
                .andExpect(jsonPath("$.players[0].name").isString())
                .andExpect(jsonPath("$.players[0].figures", notNullValue()));
    }
}
