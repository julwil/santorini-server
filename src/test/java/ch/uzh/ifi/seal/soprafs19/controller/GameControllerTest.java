package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.service.GameService;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GameResource REST resource.
 *
 * @see GameService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GameController gameController;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameRepository gameRepository;

    private User testUser;

    @Before
    public void init() throws Exception {
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setName("Test User");
        testUser.setPassword("testPassword");
        userService.createUser(testUser);
    }

    @Test
    public void getGames() throws Exception {
        Game testGame = new Game();
        testGame.setName("testGame");
        testGame.setStatus("RUNNING");
        testGame.setIsGodPower(false);
        gameService.createGame(testGame);
        Game testGame2 = new Game();
        testGame2.setName("testGame2");
        testGame2.setStatus("RUNNING");
        testGame2.setIsGodPower(true);
        gameService.createGame(testGame2);

        this.mvc.perform(get("/games")
                .header("Authorization", testUser.getToken()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.games", notNullValue()));

        gameRepository.delete(gameRepository.findById(testGame.getId()));
        gameRepository.delete(gameRepository.findById(testGame2.getId()));
    }

    @Test
    public void postGame() throws Exception {
        this.mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"TestGame\",\"status\": \"RUNNING\", \"isGodPower\": false}")
                .header("Authorization", testUser.getToken()))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.path", notNullValue()));

        gameRepository.delete(gameRepository.findByName("TestGame"));
    }

    @Test
    public void putGame() throws Exception {
        Game testGame = new Game();
        testGame.setName("testGame");
        testGame.setStatus("RUNNING");
        testGame.setIsGodPower(false);
        gameService.createGame(testGame);

        this.mvc.perform(put("/games/"+testGame.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"TestGameUpdate\"}")
                .header("Authorization", testUser.getToken()))
                .andExpect(status().is(204));

        org.junit.Assert.assertNotNull(gameRepository.findByName("TestGameUpdate"));

        gameRepository.delete(gameRepository.findById(testGame.getId()));
    }

    @Test
    public void getGame() throws Exception {
        Game testGame = new Game();
        testGame.setName("testGame");
        testGame.setStatus("RUNNING");
        testGame.setIsGodPower(false);
        gameService.createGame(testGame);

        this.mvc.perform(get("/games/"+testGame.getId())
                .header("Authorization", testUser.getToken()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").isNumber(testGame.getId()))
                .andExpect(jsonPath("$.name").isString(testGame.getName()))
                .andExpect(jsonPath("$.status").isString(testGame.getStatus()))
                .andExpect(jsonPath("$.isGodPower").isBoolean(testGame.getIsGodPower()))
                .andExpect(jsonPath("$.createTime").isNotEmpty())
                .andExpect(jsonPath("$.board").isNotEmpty())
                .andExpect(jsonPath("$.currentTurn").isNotEmpty())
        ;

        gameRepository.delete(gameRepository.findById(testGame.getId()));
    }
}
