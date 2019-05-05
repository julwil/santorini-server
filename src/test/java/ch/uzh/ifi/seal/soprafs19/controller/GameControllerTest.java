package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.GameRuleException;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.service.BuildingService;
import ch.uzh.ifi.seal.soprafs19.utilities.AuthenticationService;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.service.GameService;
import ch.uzh.ifi.seal.soprafs19.service.FigureService;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import ch.uzh.ifi.seal.soprafs19.utilities.Utilities;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static ch.uzh.ifi.seal.soprafs19.constant.UserStatus.*;
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
    private UserRepository userRepository;
    @Autowired
    private FigureRepository figureRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private GameRepository gameRepository;


    private User testUser;
    private User testUser2;
    private Game game;
    private  GameRuleException gameRuleException;
    private int size = 0;

    ServerHttpResponse serverHttpResponse;
    AuthenticationService authenticationService;

//    GameRepository gameRepository;
    @Autowired
    Utilities utils;
    @Autowired
    AuthenticationService authentication;

//    BuildingRepository buildingRepository;
    @Autowired
    UserService userService = new UserService(userRepository, authentication, utils);
    @Autowired
    GameService gameService = new GameService(gameRepository, figureRepository, userRepository,userService);
    private Object NullPointerException;
    @Autowired
    FigureService figureService = new FigureService(figureRepository, buildingRepository, gameService);
    @Autowired
    BuildingService buildingService = new BuildingService(figureRepository, buildingRepository, gameService);

//
//    @Before
//    public void init() throws Exception {
//
//
//
//        testUser = new User();
//        testUser.setUsername("testUser");
//        testUser.setName("Test User");
//        testUser.setPassword("testPassword");
//        userService.postCreateUser(testUser);
//        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));}
//
//        testUser2 = new User();
//        testUser2.setUsername("testUser2");
//        testUser2.setName("Test User2");
//        testUser2.setPassword("testPassword");
//        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser2);
//
////        userRepository.save(testUser);
////        userRepository.save(testUser2);
////
//        userService.postLogin(testUser);
//        userService.postLogin(testUser2);
//
//        String token1 = testUser.getToken();
//        long id1 = testUser.getId();
//        String token2 = testUser2.getToken();
//        long id2 = testUser2.getId();
//
//        Game game = new Game();
//        testUser.setStatus(UserStatus.ONLINE);
//        testUser2.setStatus(UserStatus.ONLINE);
//        game.setUser1(testUser);
//        game.setUser2(testUser2);
//        game.setCurrentTurn(testUser2);
//        game.setGodPower(true);
//
//        gameService.postCreateGame(game);



//        gameService.postAcceptGameRequestByUser(game.getId(),testUser2);

//
//    @After
//    public void destruct(){ userRepository.delete(testUser);
//
//    }

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
//        testUser = new User();
//        testUser.setUsername("testUser1.11");
//        testUser.setName("Test User1.11");
//        testUser.setPassword("testPassword");
//        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser);
//        userService.postLogin(testUser);
//        testUser.setStatus(ONLINE);
//
//
//        testUser2 = new User();
//        testUser2.setUsername("testUser1.22");
//        testUser2.setName("Test User1.22");
//        testUser2.setPassword("testPassword");
//        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser2);
//
//
////                userService.postLogin(testUser2);
//        testUser2.setStatus(ONLINE);
//
//
//        Game game = new Game();
//        game.setUser1(testUser);
//        game.setUser2(testUser2);
//        game.setCurrentTurn(testUser2);
//        game.setGodPower(true);
//
//
//        this.mvc.perform(get("/games/" + game.getId())
//
//                .header("Content-Type","application/json;charset=UTF-8")
//                .header("Authorization", testUser.getToken()))
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
                .header("Authorization", "token"))
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
                .header("Authorization", "token"))
                .andExpect(status().is(201))
                .andExpect(header().string("Content-Type","application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.path").isString());
    }

    @Test
    public void getPlayers() throws Exception {
        this.mvc.perform(get("/games/1/players")
                .header("Authorization", "token"))
                .andExpect(status().is(200))
                .andExpect(header().string("Content-Type","application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.players").isNotEmpty())
                .andExpect(jsonPath("$.players[0]").isNotEmpty())
                .andExpect(jsonPath("$.players[0].id").isNumber())
                .andExpect(jsonPath("$.players[0].name").isString())
                .andExpect(jsonPath("$.players[0].figures", notNullValue()));
    }

//    @Test
//    public void setFigure() throws Exception {
//        authenticationService.authenticateUser("eyJ1c2VyX2lkIjozLCJ0b2tlbl9jcmVhdGVkIjoiVGh1IE1heSAwMiAyMDo1NzozMSBDRVNUIDIwMTkifQ==");
//        authenticationService.userTokenInGameById("eyJ1c2VyX2lkIjozLCJ0b2tlbl9jcmVhdGVkIjoiVGh1IE1heSAwMiAyMDo1NzozMSBDRVNUIDIwMTkifQ==", 3);
//        authenticationService.userTokenIsCurrentTurn("eyJ1c2VyX2lkIjozLCJ0b2tlbl9jcmVhdGVkIjoiVGh1IE1heSAwMiAyMDo1NzozMSBDRVNUIDIwMTkifQ==", 3);
//        this.mvc.perform(post("/games/11/figures")
//
//                .header("Authorization", "eyJ1c2VyX2lkIjozLCJ0b2tlbl9jcmVhdGVkIjoiVGh1IE1heSAwMiAyMDo1NzozMSBDRVNUIDIwMTkifQ==")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"x\": 3,\"y\": 2, \"z\": 0}"));
//
//        authenticationService.authenticateUser("eyJ1c2VyX2lkIjozLCJ0b2tlbl9jcmVhdGVkIjoiVGh1IE1heSAwMiAyMDo1NzozMSBDRVNUIDIwMTkifQ==");
//        authenticationService.userTokenInGameById("eyJ1c2VyX2lkIjozLCJ0b2tlbl9jcmVhdGVkIjoiVGh1IE1heSAwMiAyMDo1NzozMSBDRVNUIDIwMTkifQ==", 3);
//        authenticationService.userTokenIsCurrentTurn("eyJ1c2VyX2lkIjozLCJ0b2tlbl9jcmVhdGVkIjoiVGh1IE1heSAwMiAyMDo1NzozMSBDRVNUIDIwMTkifQ==", 3);
//
//
//        Assert.assertEquals(401, serverHttpResponse);
//
//
//    }

        @Test
        public void initializeGame() throws Exception {

            testUser = new User();
            testUser.setUsername("testUser1.1");
            testUser.setName("Test User1.1");
            testUser.setPassword("testPassword");
            testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
            userService.postCreateUser(testUser);
            userService.postLogin(testUser);



            testUser2 = new User();
            testUser2.setUsername("testUser1.2");
            testUser2.setName("Test User1.2");
            testUser2.setPassword("testPassword");
            testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
            userService.postCreateUser(testUser2);


                userService.postLogin(testUser2);



            Game game = new Game();
            game.setUser1(testUser);
            game.setUser2(testUser2);
            game.setCurrentTurn(testUser2);
            game.setGodPower(true);
            gameService.postCreateGame(game);


            Assert.assertEquals(game.getStatus(), GameStatus.INITIALIZED);
            Assert.assertEquals(UserStatus.CHALLENGED, testUser2.getStatus());
            Assert.assertEquals(UserStatus.CHALLENGED, testUser.getStatus());


    }

    @Test
    public void rejectGame() throws Exception {

        testUser = new User();
        testUser.setUsername("testUser2.1");
        testUser.setName("Test User2.1");
        testUser.setPassword("testPassword");
        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
        Assert.assertEquals(NullPointerException,testUser.getStatus());
        userService.postCreateUser(testUser);
        userService.postLogin(testUser);
        testUser2 = new User();
        testUser2.setUsername("testUser2.2");
        testUser2.setName("Test User2.2");
        testUser2.setPassword("testPassword");
        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
        userService.postCreateUser(testUser2);
        userService.postLogin(testUser2);
        Game game = new Game();
        game.setUser1(testUser);
        game.setUser2(testUser2);
        game.setCurrentTurn(testUser2);
        game.setGodPower(true);
        gameService.postCreateGame(game);
        gameService.postCancelGameRequestByUser(game,testUser2);
        gameService.postCancelGameRequestByUser(game,testUser);
        Assert.assertEquals(GameStatus.CANCLED, game.getStatus());
        Assert.assertEquals(UserStatus.ONLINE, testUser2.getStatus());
        Assert.assertEquals(UserStatus.ONLINE, testUser.getStatus());


    }



    @Test
    public void acceptGameRequestAndUser2FirstPutsFiguresOnBoard() throws Exception {

        testUser = new User();
        testUser.setUsername("testUser3.1");
        testUser.setName("TestUser3.1");
        testUser.setPassword("testPassword");
        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
        userService.postCreateUser(testUser);

        testUser2 = new User();
        testUser2.setUsername("testUser3.2");
        testUser2.setName("TestUser3.2");
        testUser2.setPassword("testPassword");
        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
        userService.postCreateUser(testUser2);

        userService.postLogin(testUser);
        userService.postLogin(testUser2);

        Game game = new Game();
        game.setUser1(testUser);
        game.setUser2(testUser2);
        game.setCurrentTurn(testUser2);
        game.setGodPower(true);
        gameService.postCreateGame(game);

        long gameId= game.getId();
        Game testgame = gameService.postAcceptGameRequestByUser(gameId, testUser2);



        Assert.assertEquals(CHALLENGED, testUser2.getStatus());
        Assert.assertEquals(CHALLENGED, testUser.getStatus());
        Assert.assertEquals(testUser2, game.getCurrentTurn());

    }

    //when game starts, the first player will set 2 workers (figures), and only after player succesfully set
            //2 figures, the turn will change
    @Test
    public void afterPutting2FiguresChangeTurn() throws Exception {

        testUser = new User();
        testUser.setUsername("testUser4.1");
        testUser.setName("Test User4.1");
        testUser.setPassword("testPassword");
        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
        userService.postCreateUser(testUser);

        testUser2 = new User();
        testUser2.setUsername("testUser4.2");
        testUser2.setName("Test User4.2");
        testUser2.setPassword("testPassword");
        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));

        userService.postCreateUser(testUser2);

        userService.postLogin(testUser);
        userService.postLogin(testUser2);
        Game game = new Game();


        game.setUser1(testUser);
        game.setUser2(testUser2);
        game.setCurrentTurn(testUser2);
        game.setGodPower(true);
        String str1 = gameService.postCreateGame(game);

        long gameId = game.getId();
        Game g = gameService.postAcceptGameRequestByUser(gameId, testUser2);

        Figure figure = new Figure();
        Figure figure2 = new Figure();
        Position position1 = new Position(2, 2, 0);
        Position position2 = new Position(3, 3, 0);

        Assert.assertEquals(testUser2, game.getCurrentTurn());

        figure.setPosition(position1);
        figure.setOwnerId(testUser2.getId());
        figure.setGame(game);
        figureService.postGameBoardFigure(game, figure);

        Assert.assertEquals(testUser2, game.getCurrentTurn());

        figure2.setPosition(position2);
        figure2.setOwnerId(testUser2.getId());
        figure2.setGame(game);
        figureService.postGameBoardFigure(game, figure2);
        figureService.getGameBoardFigures(game).forEach(figure1 -> ++size);

        Assert.assertEquals(2, size);
        Assert.assertEquals(testUser, game.getCurrentTurn());


    }



//        When game starts, the first player to set workers will first move a worker and after that build.
//         and not the other way around. Always first move, than build (classic game-NoGodcardsInvolved)
        @Test
        public void afterBothPlayersSetWorkersFirstPlayerWillStartByMovingOneWorker() throws Exception {

            testUser = new User();
            testUser.setUsername("testUser5.1");
            testUser.setName("Test User5.1");
            testUser.setPassword("testPassword");
            testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
            userService.postCreateUser(testUser);
            testUser2 = new User();
            testUser2.setUsername("testUser5.2");
            testUser2.setName("Test User5.2");
            testUser2.setPassword("testPassword");
            testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
            userService.postCreateUser(testUser2);
            userService.postLogin(testUser);
            userService.postLogin(testUser2);
            Game game = new Game();
            testUser.setStatus(ONLINE);
            testUser2.setStatus(ONLINE);
            game.setUser1(testUser);
            game.setUser2(testUser2);
            game.setCurrentTurn(testUser2);
            game.setGodPower(true);
            String str1 = gameService.postCreateGame(game);
            long gameId= game.getId();
            Game g = gameService.postAcceptGameRequestByUser(gameId, testUser2);
            Figure figure = new Figure();
            Figure figure2 = new Figure();
            Figure figure3 = new Figure();
            Figure figure4 = new Figure();

            Position position1 = new Position(2,2,0); //testUser2
            Position position2 = new Position(3,3,0); // testUser2
            Position position3 = new Position(1,1,0); // testUser
            Position position4 = new Position(3,2,0); // testUser
            Position positionb1 = new Position(2,0,0);
            Position positionb2 = new Position(2,1,0);

            figure.setPosition(position1);
            figure.setOwnerId(testUser2.getId());
            figure.setGame(game);
            figureService.postGameBoardFigure(game, figure);

            figure2.setPosition(position2);
            figure2.setOwnerId(testUser2.getId());
            figure2.setGame(game);
            figureService.postGameBoardFigure(game, figure2);


            figure3.setPosition(position3);
            figure3.setOwnerId(testUser.getId());
            figure3.setGame(game);
            figureService.postGameBoardFigure(game, figure3);
            figure4.setPosition(position4);
            figure4.setOwnerId(testUser.getId());
            figure4.setGame(game);
            figureService.postGameBoardFigure(game, figure4);

            Assert.assertEquals(testUser2, game.getCurrentTurn());
            figureService.putGameBoardFigure(game,figure, positionb2);

            Building building = new Building();
            building.setPosition(positionb1);
            building.setOwnerId(testUser2.getId());
            building.setGame(game);

            buildingService.postGameBoardBuilding(game, building);
            Assert.assertEquals(buildingService.getGameBoardBuildings(game).iterator().next(), building);
            Assert.assertEquals(testUser, game.getCurrentTurn());


}


     //(expected = GameRuleException.class)





    }




