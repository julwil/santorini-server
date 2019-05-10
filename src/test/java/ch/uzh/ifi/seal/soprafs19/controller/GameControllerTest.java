package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.*;
import ch.uzh.ifi.seal.soprafs19.exceptions.*;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.service.game.service.BuildingService;
import ch.uzh.ifi.seal.soprafs19.utilities.AuthenticationService;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.service.game.service.GameService;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.actions.moves.DefaultMoves;
import ch.uzh.ifi.seal.soprafs19.service.game.service.FigureService;
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

import java.lang.reflect.Array;
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
    @Autowired
    private MoveRepository moveRepository;




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
    GameService gameService = new GameService(gameRepository, figureRepository, moveRepository ,buildingRepository, userRepository, userService);
    private Object NullPointerException;
    @Autowired
    FigureService figureService = new FigureService(figureRepository, buildingRepository, moveRepository, gameRepository, gameService  );
    @Autowired
    BuildingService buildingService = new BuildingService(buildingRepository, figureService, gameService);

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
//
//    @Test
//    public void getGame() throws Exception {
//        testUser = new User();
//        testUser.setUsername("testUser1.11");
//        testUser.setName("Test User1.11");
//        testUser.setPassword("testPassword");
//        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser);
//        testUser.setStatus(ONLINE);
//        testUser.setToken("123123123");
//
//
//
//
//        testUser2 = new User();
//        testUser2.setUsername("testUser1.22");
//        testUser2.setName("Test User1.22");
//        testUser2.setPassword("testPassword");
//        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser2);
//        testUser2.setStatus(ONLINE);
//
//
//        this.mvc.perform(get("/games/")
//
//                .header("Content-Type","application/json;charset=UTF-8")
//                .header("Authorization", testUser.getToken())
//                .content("{\"user1\": testuser1,\"user2\": testuser2, \"isGodPower\": false}"))
//                .andExpect(jsonPath("$.id").isNotEmpty())
//                .andExpect(jsonPath("$.name").isString())
//                .andExpect(jsonPath("$.status").isString())
//                .andExpect(jsonPath("$.isGodPower").isBoolean())
//                .andExpect(jsonPath("$.createTime").isNotEmpty())
//                .andExpect(jsonPath("$.board").isNotEmpty())
//                .andExpect(jsonPath("$.currentTurn").isNotEmpty())
//                .andExpect(jsonPath("$.currentTurn.id").isNumber())
//                .andExpect(jsonPath("$.currentTurn.name").isString())
//                .andExpect(jsonPath("$.currentTurn.figures", notNullValue()));
//
//    }

//    @Test
//    public void getTurns() throws Exception {
//        testUser = new User();
//        testUser.setUsername("testUser13");
//        testUser.setName("Test User13");
//        testUser.setPassword("testPassword");
//        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser);
//        testUser2 = new User();
//        testUser2.setUsername("testUser133");
//        testUser2.setName("Test User133");
//        testUser2.setPassword("testPassword");
//        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//
//        userService.postCreateUser(testUser2);
//        userService.postLogin(testUser);
//        userService.postLogin(testUser2);
//
//        Game game = new Game();
//        game.setUser1(testUser);
//        game.setUser2(testUser2);
//        game.setCurrentTurn(testUser2);
//        game.setGodPower(true);
//        gameService.postCreateGame(game);
//        String token = testUser.getToken();
//
//        this.mvc.perform(get("/games/"+game.getId()+"/turns")
//                .header("Authorization", token)
//                .header("Content-Type","application/json;charset=UTF-8"))
//                .andExpect(status().is(200))
//                .andExpect(header().string("Content-Type","application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.turns").isNotEmpty())
//                .andExpect(jsonPath("$.turns[0]").isNotEmpty())
//                .andExpect(jsonPath("$.turns[0].id").isNumber())
//                .andExpect(jsonPath("$.turns[0].createTime").isString())
//                .andExpect(jsonPath("$.turns[0].performedBy").isNotEmpty())
//                .andExpect(jsonPath("$.turns[0].performedBy.id").isNumber())
//                .andExpect(jsonPath("$.turns[0].performedBy.name").isString())
//                .andExpect(jsonPath("$.turns[0].performedBy.figures", notNullValue()))
//                .andExpect(jsonPath("$.turns[0].finished").isBoolean())
//                .andExpect(jsonPath("$.turns[0].events", notNullValue()));
//
//
//    }

//    @Test
//    public void postTurn() throws Exception { //----------------------??
//        testUser = new User();
//        testUser.setUsername("testUser111");
//        testUser.setName("Test User111");
//        testUser.setPassword("testPassword");
//        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser);
//        testUser2 = new User();
//        testUser2.setUsername("testUser122");
//        testUser2.setName("Test User122");
//        testUser2.setPassword("testPassword");
//        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//
//        userService.postCreateUser(testUser2);
//
//        userService.postLogin(testUser);
//        userService.postLogin(testUser2);
//
//        Game game = new Game();
//        game.setUser1(testUser);
//        game.setUser2(testUser2);
//        game.setCurrentTurn(testUser2);
//        game.setGodPower(true);
//        gameService.postCreateGame(game);
//        System.out.println(game.getId());
//
//        System.out.println(testUser.getId());
//        String token = testUser.getToken();
//
//        System.out.println(testUser.getToken());
//        this.mvc.perform(post("/games/"+game.getId()+"/turns")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"performedBy\": 1,\"finished\": true, \"events\": [\"event1\",\"event2\"]}")
//                .header("Authorization", token))
//                .andExpect(status().is(201))
//                .andExpect(header().string("Content-Type","application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.path").isString());
//
//
//    }

//    @Test
//    public void getPlayers() throws Exception {
//        testUser = new User();
//        testUser.setUsername("testUser11");
//        testUser.setName("Test User11");
//        testUser.setPassword("testPassword");
//        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser);
//        testUser2 = new User();
//        testUser2.setUsername("testUser12");
//        testUser2.setName("Test User12");
//        testUser2.setPassword("testPassword");
//        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//
//        userService.postCreateUser(testUser2);
//        userService.postLogin(testUser);
//        userService.postLogin(testUser2);
//
//        Game game = new Game();
//        game.setUser1(testUser);
//        game.setUser2(testUser2);
//        game.setCurrentTurn(testUser2);
//        game.setGodPower(true);
//
//        gameService.postCreateGame(game);
//        String token = testUser.getToken();
//
//        this.mvc.perform(get("/games/"+game.getId()+"/players")
//
//                .header("Authorization", token)
//                .header("Content-Type","application/json;charset=UTF-8"))
//
//                .andExpect(status().is(200))
//                .andExpect(header().string("Content-Type","application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.players").isNotEmpty())
//                .andExpect(jsonPath("$.players[0]").isNotEmpty())
//                .andExpect(jsonPath("$.players[0].id").isNumber())
//                .andExpect(jsonPath("$.players[0].name").isString())
//                .andExpect(jsonPath("$.players[0].figures", notNullValue()));
//
//
//    }

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
//
//    @Test
//    public void rejectGame() throws Exception {
//
//        testUser = new User();
//        testUser.setUsername("testUser2.1");
//        testUser.setName("Test User2.1");
//        testUser.setPassword("testPassword");
//        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser);
//        userService.postLogin(testUser);
//        testUser2 = new User();
//        testUser2.setUsername("testUser2.2");
//        testUser2.setName("Test User2.2");
//        testUser2.setPassword("testPassword");
//        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser2);
//        userService.postLogin(testUser2);
//
//        Game game = new Game();
//        game.setUser1(testUser);
//        game.setUser2(testUser2);
//        game.setCurrentTurn(testUser2);
//        game.setGodPower(true);
//        gameService.postCreateGame(game);
//
//        gameService.postCancelGameRequestByUser(game.getId(),testUser2);
//
////        Assert.assertEquals(GameStatus.CANCLED, game.getStatus());
//        Assert.assertEquals(UserStatus.ONLINE, testUser2.getStatus());
//        Assert.assertEquals(UserStatus.ONLINE, testUser.getStatus());
//
//
//    }



    @Test
    public void acceptGameRequestAndUser2FirstPutsFiguresOnBoard() throws Exception  {

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

        System.out.print(" st1 "+ testUser2.getStatus());

        Game game = new Game();
        game.setUser1(testUser);
        game.setUser2(testUser2);
        game.setCurrentTurn(testUser2);
        game.setGodPower(true);

        gameService.postCreateGame(game); //tampoco weil testuser are offline

        long gameId= game.getId();
        Game testgame = gameService.postAcceptGameRequestByUser(gameId, testUser2);



        Assert.assertEquals(CHALLENGED, testUser2.getStatus());
        Assert.assertEquals(CHALLENGED, testUser.getStatus());
        Assert.assertEquals(testUser2, game.getCurrentTurn());


    }

//    when game starts, the first player will set 2 workers (figures), and only after player succesfully set
//    2 figures, the turn will change
    @Test
    public void afterPutting2FiguresChangeTurn() throws Exception {


        // create 2 users & automatically assign Id
        testUser = new User();
        testUser.setUsername("testUser41");
        testUser.setName("Test User41");
        testUser.setPassword("testPassword");
        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));

        userService.postCreateUser(testUser); // this method creates user and sets status to: OFFLINE
        Assert.assertEquals(OFFLINE,testUser.getStatus());
        Assert.assertEquals(testUser.getToken(), null); // offline = no token

        testUser2 = new User();
        testUser2.setUsername("testUser42");
        testUser2.setName("Test User42");
        testUser2.setPassword("testPassword");
        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));

        userService.postCreateUser(testUser2);

        userService.postLogin(testUser2); // this logs user in. Status will be online & Token created
        userService.postLogin(testUser);


        Game game = new Game();          // create game and assign id
        game.setUser1(testUser);
        game.setUser2(testUser2);
        game.setCurrentTurn(testUser2);
        game.setGodPower(false);

        Assert.assertNotEquals(GameStatus.INITIALIZED, game.getStatus()); // not Initialized


        gameService.postCreateGame(game); // game status = INITIALIZED & users CHALLENGED
        //not the game is in the gameRepository

        long gameId = game.getId();

        Assert.assertEquals(GameStatus.INITIALIZED, gameRepository.findById(gameId).getStatus()); // now it is initialized
        Assert.assertNotEquals(CHALLENGED, gameRepository.findById(gameId).getUser1());
        Assert.assertNotEquals(CHALLENGED, gameRepository.findById(gameId).getUser2());



        // after accepting -> game will be STARTED & players will be PLAYING
        gameService.postAcceptGameRequestByUser(gameId, testUser2);

        Assert.assertEquals(GameStatus.STARTED, gameRepository.findById(gameId).getStatus()); // now it is initialized
        Assert.assertNotEquals(PLAYING, gameRepository.findById(gameId).getUser1());
        Assert.assertNotEquals(PLAYING, gameRepository.findById(gameId).getUser2());



        Figure figure = new Figure();
        Figure figure2 = new Figure();
        Position position1 = new Position(2, 2, 0);
        Position position2 = new Position(3, 3, 0);

        // first player in putting figures
        Assert.assertEquals(testUser2.getId(), gameRepository.findById(gameId).getCurrentTurn().getId());

        figure.setPosition(position1);
        figure.setOwnerId(testUser2.getId());
        figure.setGame(game);
        figureService.postFigure(game, figure);

        Assert.assertEquals(testUser2.getId(), gameRepository.findById(gameId).getCurrentTurn().getId());

        figure2.setPosition(position2);
        figure2.setOwnerId(testUser2.getId());
        figure2.setGame(game);
        figureService.postFigure(game, figure2);

        // after the first one puts 2 figures, the turn automatically changes
        Assert.assertEquals(testUser.getId(), gameRepository.findById(gameId).getCurrentTurn().getId());


    }

    @Test
    public void moveWorkerfterPlacingOnly1WorkerFails() throws Exception {

        testUser = new User();
        testUser.setUsername("testUser44.1");
        testUser.setName("Test User44.1");
        testUser.setPassword("testPassword");
        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
        userService.postCreateUser(testUser);
        testUser2 = new User();
        testUser2.setUsername("testUser44.2");
        testUser2.setName("Test User44.2");
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
        figureService.postFigure(game, figure);
        Position targetPosition = new Position(2,1,0);
        try {
            figureService.putFigure(figure.getId(), targetPosition);
        }
        catch (GameRuleException e){
            Assert.assertEquals("Game rule violation", e.getMessage());


        }


    }


    //        When game starts, the first player to set workers will first move a worker and after that build.
//         and not the other way around. Always first move, than build (classic game-NoGodcardsInvolved)
//    @Test
//    public void afterBothPlayersSetWorkersFirstPlayerWillStartByMovingOneWorker() throws Exception {
//
//        testUser = new User();
//        testUser.setUsername("testUser5.1");
//        testUser.setName("Test User5.1");
//        testUser.setPassword("testPassword");
//        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser);
//
//        testUser2 = new User();
//        testUser2.setUsername("testUser5.2");
//        testUser2.setName("Test User5.2");
//        testUser2.setPassword("testPassword");
//        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
//        userService.postCreateUser(testUser2);
//        userService.postLogin(testUser);
//        userService.postLogin(testUser2);
//        testUser.setStatus(PLAYING);
//        testUser2.setStatus(PLAYING);
//
//        Game game = new Game();
//
//        game.setUser1(testUser);
//        game.setUser2(testUser2);
//        game.setCurrentTurn(testUser2);
//        game.setGodPower(true);
//
//        String str1 = gameService.postCreateGame(game);
//
//        long gameId= game.getId();
//        Game g = gameService.postAcceptGameRequestByUser(gameId, testUser2);
//
//        Figure figure = new Figure();
//        Figure figure2 = new Figure();
//        Figure figure3 = new Figure();
//        Figure figure4 = new Figure();
//
//        Position position1 = new Position(2,2,0); //testUser2
//        Position position2 = new Position(3,3,0); // testUser2
//        Position position3 = new Position(1,1,0); // testUser
//        Position position4 = new Position(3,2,0); // testUser
//        Position positionb1 = new Position(2,0,0);
//        Position positionb2 = new Position(2,1,0);
//
//        figure.setPosition(position1);
//        figure.setOwnerId(game.getUser2().getId());
//        figure.setGame(game);
//        figureService.postFigure(game, figure);
//
//        figure2.setPosition(position2);
//        figure2.setOwnerId(game.getUser2().getId());
//        figure2.setGame(game);
//        figureService.postFigure(game, figure2);
//
//        Assert.assertEquals(game.getUser2(), game.getCurrentTurn());
//        figure3.setPosition(position3);
//        figure3.setOwnerId(game.getUser1().getId());
//        figure3.setGame(game);
//        figureService.postFigure(game, figure3);
//
//        figure4.setPosition(position4);
//        figure4.setOwnerId(game.getUser1().getId());
//        figure4.setGame(game);
//        figureService.postFigure(game, figure4);
//
//        Assert.assertEquals(game.getUser2(), game.getCurrentTurn());
//
//        figureService.putFigure(figure.getId(), positionb2);
//
//        Building building = new Building();
//        building.setPosition(positionb1);
//        building.setOwnerId(game.getUser2().getId());
//        building.setGame(game);
//
//
//
//        buildingService.postBuilding(game, building);
//
//        Assert.assertEquals(buildingService.getAllBuildings(game).iterator().next(), building);
//        Assert.assertEquals(game.getUser1(), game.getCurrentTurn());
//
//
//
//    }



    //(expected = GameRuleException.class)
    @Test
    public void demoTest() throws Exception {
        testUser = new User();
        testUser.setUsername("testUser4.11");
        testUser.setName("Test User4.11");
        testUser.setPassword("testPassword");
        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));


        userService.postCreateUser(testUser);
        testUser2 = new User();
        testUser2.setUsername("testUser412");
        testUser2.setName("Test User412");
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
        Game newGame = gameService.postAcceptGameRequestByUser(gameId, testUser2);

        User testUser = newGame.getUser1();
        User testUser2 = newGame.getUser2();

        newGame.setStatus(GameStatus.STARTED);
        newGame.setCurrentTurn(testUser2);
        gameRepository.save(newGame);

        testUser.setGame(newGame);
        testUser.setStatus(UserStatus.PLAYING);
        userRepository.save(testUser);

        testUser2.setGame(newGame);
        testUser2.setStatus(UserStatus.PLAYING);
        userRepository.save(testUser2);


        newGame.setUser1(testUser);
        newGame.setUser2(testUser2);
        newGame.setCurrentTurn(testUser2);
        newGame.setGodPower(false);

    Game g = gameService.postAcceptGameRequestByUser(gameId, testUser2);

    Figure figure11 = new Figure();
    Figure figure12 = new Figure();
    Figure figure21 = new Figure();
    Figure figure22 = new Figure();

    Position p220 = new Position(2,2,0); //testUser2
    Position p330 = new Position(3,3,0); // testUser2
    Position p110 = new Position(1,1,0); // testUser
    Position p320 = new Position(3,2,0); // testUser
    Position p210 = new Position(2,1,0);
    Position p221 = new Position(2,2,1);
    Position p230 = new Position(2,3,0);
    Position p231 = new Position(2,3,1);
    Position p120 = new Position(1,2,0);
    Position p310 = new Position(3,1,0);
    Position p321 = new Position(3,2,1);
    Position p211 = new Position(2,1,1);
    Position p121 = new Position(1,2,1);
    Position p222 = new Position(2,2,2);
    Position p020 = new Position(0,2,0);
    Position p420 = new Position(4,2,0);
    Position p430 = new Position(4,3,0);
    Position p213 = new Position(2,1,3);
    Position p241 = new Position(2,4,1);
    Position p212 = new Position(2,1,2);
    Position p240 = new Position(2,4,0);
    Position p232 = new Position(2,3,2);
    Position p021 = new Position(0,2,1);
    Position p122 = new Position(1,2,2);
    Position p123 = new Position(1,2,3);
    Position p322 = new Position(3,2,2);
    Position p421 = new Position(4,2,1);



        figure11.setPosition(p220);
        figure11.setOwnerId(testUser2.getId());
        figure11.setGame(newGame);
    String s1= figureService.postFigure(newGame, figure11);

        figure12.setPosition(p330);
        figure12.setOwnerId(testUser2.getId());
        figure12.setGame(newGame);
        figureService.postFigure(newGame, figure12);

        ArrayList emptyArray = new ArrayList<Position>();

        Assert.assertEquals(buildingService.getPossibleBuilds(game), emptyArray);


        figure21.setPosition(p110);
        figure21.setOwnerId(testUser.getId());
        figure21.setGame(newGame);
        figureService.postFigure(newGame, figure21);

        figure22.setPosition(p320);
        figure22.setOwnerId(testUser.getId());
        figure22.setGame(newGame);
        figureService.postFigure(newGame,figure22);

        Assert.assertEquals(buildingService.getPossibleBuilds(game), emptyArray);
//        Assert.assertEquals(figure11.

        figureService.putFigure(figure11.getId(),p210 );

    Building building = new Building();
        building.setPosition(p220);
        building.setOwnerId(testUser2.getId());
        building.setGame(newGame);
        buildingService.postBuilding(newGame, building);

        figureService.putFigure(figure21.getId(), p221);

    Building building2 = new Building();
        building2.setPosition(p230);
        building2.setOwnerId(testUser.getId());
        building2.setGame(newGame);
        buildingService.postBuilding(newGame, building2);

        figureService.putFigure(figure12.getId(), p231);

    Building building3 = new Building();
        building3.setPosition(p120);
        building3.setOwnerId(testUser2.getId());
        building3.setGame(newGame);
        buildingService.postBuilding(newGame, building3);

        figureService.putFigure(figure22.getId(), p310);


    Building building4 = new Building();
        building4.setPosition(p320);
        building4.setOwnerId(testUser.getId());
        building4.setGame(newGame);
        buildingService.postBuilding(newGame, building4); //move

        figureService.putFigure(figure11.getId(), p321);

    Building building5 = new Building();
        building5.setPosition(p210);
        building5.setOwnerId(testUser2.getId());
        building5.setGame(newGame);
        buildingService.postBuilding(newGame, building5);

        figureService.putFigure(figure22.getId(), p211);

    Building building6 = new Building();
        building6.setPosition(p121);
        building6.setOwnerId(testUser.getId());
        building6.setGame(newGame);

        buildingService.postBuilding(newGame, building6);
        figureService.putFigure(figure12.getId(), p122);

    Building building7 = new Building();
        building7.setPosition(p231);
        building7.setOwnerId(testUser2.getId());
        building7.setGame(newGame);
        buildingService.postBuilding(newGame, building7);
        figureService.putFigure(figure21.getId(), p232);

    Building building8 = new Building();
        building8.setPosition(p221);
        building8.setOwnerId(testUser.getId());
        building8.setGame(newGame);

        buildingService.postBuilding(newGame, building8);
        figureService.putFigure(figure11.getId(), p222);

    Building building9 = new Building();
        building9.setPosition(p321);
        building9.setOwnerId(testUser2.getId());
        building9.setGame(newGame);
        buildingService.postBuilding(newGame, building9);
        figureService.putFigure(figure22.getId(), p322);

    Building building10 = new Building();
        building10.setPosition(p420);
        building10.setOwnerId(testUser.getId());
        building10.setGame(newGame);
        buildingService.postBuilding(newGame, building10);
        figureService.putFigure(figure12.getId(), p110);

    Building building11 = new Building();
        building11.setPosition(p020);
        building11.setOwnerId(testUser2.getId());
        building11.setGame(newGame);
        buildingService.postBuilding(newGame, building11);
        figureService.putFigure(figure22.getId(), p430);

    Building building12 = new Building();
        building12.setPosition(p421);
        building12.setOwnerId(testUser.getId());
        building12.setGame(newGame);
        buildingService.postBuilding(newGame, building12);
        figureService.putFigure(figure11.getId(), p322);

    Building building13 = new Building();
        building13.setPosition(p211);
        building13.setOwnerId(testUser2.getId());
        building13.setGame(newGame);
        buildingService.postBuilding(newGame, building13);
        figureService.putFigure(figure21.getId(), p222);

    Building building14 = new Building();
        building14.setPosition(p122);
        building14.setOwnerId(testUser.getId());
        building14.setGame(newGame);
        buildingService.postBuilding(newGame, building14);

        figureService.putFigure(figure12.getId(), p021);

    Building building15 = new Building();
        building15.setPosition(p123);
        building15.setOwnerId(testUser2.getId());
        building15.setGame(newGame);
        buildingService.postBuilding(newGame, building15);

        figureService.putFigure(figure21.getId(), p232);

        Building building16 = new Building();
        building16.setPosition(p240);
        building16.setOwnerId(testUser.getId());
        building16.setGame(newGame);
        buildingService.postBuilding(newGame, building16);
        figureService.putFigure(figure11.getId(), p222);

        Building building17 = new Building();
        building17.setPosition(p212);
        building17.setOwnerId(testUser2.getId());
        building17.setGame(newGame);
        buildingService.postBuilding(newGame, building17);

        figureService.putFigure(figure22.getId(), p330);

        Building building18 = new Building();
        building18.setPosition(p241);
        building18.setOwnerId(testUser.getId());
        building18.setGame(newGame);
        buildingService.postBuilding(newGame, building18);



        Assert.assertEquals(0,gameRepository.findById(gameId).getWinner());

        figureService.putFigure(figure11.getId(), p213); // testUser wins

        long testUser2Id= testUser2.getId();

        Assert.assertEquals(testUser2Id , gameRepository.findById(gameId).getWinner());



        Assert.assertEquals(GameStatus.FINISHED, gameRepository.findById(gameId).getStatus());

}

}
