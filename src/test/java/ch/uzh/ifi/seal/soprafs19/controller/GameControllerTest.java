package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.utilities.AuthenticationService;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.service.GameService;
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

import static ch.uzh.ifi.seal.soprafs19.constant.UserStatus.PLAYING;
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


    private User testUser;
    private User testUser2;
    private Game game;

    ServerHttpResponse serverHttpResponse;
    AuthenticationService authenticationService;

    GameRepository gameRepository;
    Utilities utils;
    AuthenticationService authentication;

    @Autowired
    UserService userService = new UserService(userRepository, authentication, utils);
    @Autowired
    GameService gameService = new GameService(gameRepository, figureRepository, userRepository,userService);
    private Object NullPointerException;

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
//        Assert.assertEquals(401,serverHttpResponse);




        @Test
        public void initializeGame() throws Exception {

            testUser = new User();
            testUser.setUsername("testUser");
            testUser.setName("Test User");
            testUser.setPassword("testPassword");
            userService.postCreateUser(testUser);
            testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
            testUser2 = new User();
            testUser2.setUsername("testUser2");
            testUser2.setName("Test User2");
            testUser2.setPassword("testPassword");
            testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
            userService.postCreateUser(testUser2);

            userRepository.save(testUser);
            userRepository.save(testUser2);

            userService.postLogin(testUser);
            userService.postLogin(testUser2);

            Game game = new Game();

            testUser.setStatus(UserStatus.ONLINE);
            testUser2.setStatus(UserStatus.ONLINE);

            game.setUser1(testUser);
            game.setUser2(testUser2);
            game.setCurrentTurn(testUser2);
            game.setGodPower(true);

            gameService.postCreateGame(game);
            long gameId= game.getId();

            Assert.assertEquals(game.getStatus(), GameStatus.INITIALIZED);


    }


    @Test
    public void acceptGameRequest() throws Exception {

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setName("Test User");
        testUser.setPassword("testPassword");
        userService.postCreateUser(testUser);
        testUser.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
        testUser2 = new User();
        testUser2.setUsername("testUser2");
        testUser2.setName("Test User2");
        testUser2.setPassword("testPassword");
        testUser2.setBirthday(new SimpleDateFormat("yy-MM-dd").parse("1948-04-06"));
        userService.postCreateUser(testUser2);

        userRepository.save(testUser);
        userRepository.save(testUser2);

        userService.postLogin(testUser);
        userService.postLogin(testUser2);

        String token1 = testUser.getToken();
        long id1 = testUser.getId();
        String token2 = testUser2.getToken();
        long id2 = testUser2.getId();

        Game game = new Game();
        testUser.setStatus(UserStatus.ONLINE);
        testUser2.setStatus(UserStatus.ONLINE);
        game.setUser1(testUser);
        game.setUser2(testUser2);
        game.setCurrentTurn(testUser2);
        game.setGodPower(true);
        gameService.postCreateGame(game);
        User user2 = userRepository.findByUsername("testUser2");
        long gameId= game.getId();
        Game testgame = gameService.postAcceptGameRequestByUser(gameId, testUser2);
        User u1= testgame.getUser1();
        User u2 = testgame.getUser2();
        System.out.print(testUser.getStatus());

        Assert.assertEquals(GameStatus.STARTED, testgame.getStatus());
        Assert.assertEquals(UserStatus.PLAYING, u2.getStatus());
        Assert.assertEquals(UserStatus.PLAYING, u1.getStatus());


    }




//    package ch.uzh.ifi.seal.soprafs19.service;
//import ch.uzh.ifi.seal.soprafs19.Application;
//import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
//import ch.uzh.ifi.seal.soprafs19.constant.GodCard;
//import ch.uzh.ifi.seal.soprafs19.constant.Mode;
//import ch.uzh.ifi.seal.soprafs19.entity.Game;
//import ch.uzh.ifi.seal.soprafs19.entity.Space;
//import ch.uzh.ifi.seal.soprafs19.entity.User;
//import ch.uzh.ifi.seal.soprafs19.entity.Worker;
//import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
//import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
//import ch.uzh.ifi.seal.soprafs19.repository.WorkerRepository;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import java.util.List;
//    @WebAppConfiguration
//    @RunWith(SpringJUnit4ClassRunner.class)
//    @SpringBootTest(classes = Application.class)
//    public class GameAdminServiceTest {
//        @Qualifier("gameRepository")
//        @Autowired
//        private GameRepository gameRepository;
//        @Qualifier("userRepository")
//        @Autowired
//        private UserRepository userRepository;
//        @Qualifier("workerRepository")
//        @Autowired
//        private WorkerRepository workerRepository;
//        @Autowired
//        private UserService userService;
//        @Autowired
//        private GameAdminService gameAdminService;
//        private User playerOne;
//        private User playerTwo;
//        private Game newGame;
//        @Before
//        public void initPlayers() throws Exception {
//            System.out.println("@Before executing, init players");
//            playerOne = new User();
//            playerOne.setUsername("playerOneUN");
//            playerOne.setName("playerOneN");
//            playerOne.setPassword("playerOnePW");
//            playerOne.setAge(20);
//            playerTwo = new User();
//            playerTwo.setUsername("playerTwoUN");
//            playerTwo.setName("playerTwoN");
//            playerTwo.setPassword("playerTwoPW");
//            playerTwo.setAge(20);
//            newGame = new Game();
//            newGame.setGameMode(Mode.NORMAL);
//            newGame.setGameName("createdGame");
//        }
//        @Test
//        public void createGameNormalModeSuccessTest(){
//            // need for having at least one user
//            Assert.assertNull(gameRepository.findByGameName("createdGame"));
//            playerOne = userService.createUser(playerOne);
//            Game createdGame = gameAdminService.createGame(playerOne.getId(), playerOne, newGame);
//            Assert.assertEquals(createdGame.getCurrentPlayerIndex(), playerOne.getId());
//            Assert.assertEquals(Mode.NORMAL,createdGame.getGameMode());
//            Assert.assertEquals(GodCard.NORMAL, createdGame.getTurnTypes().get(0));
//            Assert.assertEquals(playerOne, createdGame.getPlayers().get(0));
//            Assert.assertEquals("createdGame",createdGame.getGameName());
//            Assert.assertEquals(GameStatus.PENDING, createdGame.getGameStatus());
//            Assert.assertNotNull(createdGame.getGameId());
//            Assert.assertEquals(25, createdGame.getBoard().size());
//            Assert.assertEquals(1, createdGame.getBoard().get(1).getCoordinate().getY());
//            Assert.assertEquals(0, createdGame.getBoard().get(1).getCoordinate().getX());
//            Assert.assertNull(createdGame.getCurrentTurn());
//            Assert.assertNull(createdGame.getWinner());
//            // Assert.assertEquals(createdGame,playerOne.getCurrentGame());
//            Assert.assertEquals(createdGame,userRepository.findByToken(playerOne.getToken()).getCurrentGame());
//            userRepository.delete(playerOne);
//            gameRepository.delete(createdGame);
//        }
//        @Test
//        public void joinGameSuccessTest(){
//            playerOne = userService.createUser(playerOne);
//            playerTwo = userService.createUser(playerTwo);
//            Game createdGame = gameAdminService.createGame(playerOne.getId(), playerOne, newGame);
//            Game joinedGame = gameAdminService.joinGame(playerTwo.getId(), createdGame.getGameId(), playerTwo);
//            Assert.assertEquals(playerTwo,joinedGame.getPlayers().get(1));
//            Assert.assertEquals(GameStatus.PRERUNNING,joinedGame.getGameStatus());
//            Assert.assertEquals(joinedGame, userRepository.findByToken(playerTwo.getToken()).getCurrentGame());
//            // test here for playerTwo.getCurrentGame();
//            userRepository.delete(playerOne);
//            userRepository.delete(playerTwo);
//            gameRepository.delete(createdGame);
//        }
//        @Test
//        public void placeWorkersInitiallyTest(){
//            playerOne = userService.createUser(playerOne);
//            playerTwo = userService.createUser(playerTwo);
//            Game createdGame = gameAdminService.createGame(playerOne.getId(), playerOne, newGame);
//            Game joinedGame = gameAdminService.joinGame(playerTwo.getId(), createdGame.getGameId(), playerTwo);
//            Worker[] workers = gameAdminService.placeWorkersInitially(playerOne,joinedGame.getGameId(),0,1,4,3);
//            Assert.assertEquals(0,workers[0].getCurrentSpace().getCoordinate().getX());
//            Assert.assertEquals(1,workers[0].getCurrentSpace().getCoordinate().getY());
//            Assert.assertEquals(4,workers[1].getCurrentSpace().getCoordinate().getX());
//            Assert.assertEquals(3,workers[1].getCurrentSpace().getCoordinate().getY());
//            Assert.assertEquals(joinedGame.getGameId(),workers[0].getGameId());
//            Assert.assertEquals(joinedGame.getPlayers().get(0).getId(),workers[0].getUserId());
//            Game game = gameRepository.findByGameId(joinedGame.getGameId());
//            Assert.assertEquals(workers[1].getWorkerId(),game.getBoard().get(23).getWorkerId());
//            Assert.assertEquals(true,game.getBoard().get(5*4+3).getIsOccupied());
//            Assert.assertEquals(workers[1].getCurrentSpace().getWorkerId(), workers[1].getWorkerId());
//            Assert.assertEquals(true,game.getBoard().get(workers[1].getCurrentSpace().getCoordinate().getX()*5+
//                    workers[1].getCurrentSpace().getCoordinate().getY()).getIsOccupied());
//            workerRepository.delete(workers[0]);
//            workerRepository.delete(workers[1]);
//            userRepository.delete(playerOne);
//            userRepository.delete(playerTwo);
//            gameRepository.delete(joinedGame);
//        }
//        @Test
//        public void setWorkersAttributesTest(){
//            playerOne = userService.createUser(playerOne);
//            playerTwo = userService.createUser(playerTwo);
//            Game createdGame = gameAdminService.createGame(playerOne.getId(), playerOne, newGame);
//            Game joinedGame = gameAdminService.joinGame(playerTwo.getId(), createdGame.getGameId(), playerTwo);
//            Worker worker = new Worker();
//            worker = gameAdminService.setWorkerAttributes(worker,playerOne,joinedGame.getGameId(),0,1);
//            Assert.assertEquals(joinedGame.getGameId(),worker.getGameId());
//            Assert.assertEquals(playerOne.getId(), worker.getUserId());
//            //Assert.assertEquals(joinedGame.getBoard().get(0*5+1),worker.getCurrentSpace());
//            workerRepository.delete(worker);
//            gameRepository.delete(joinedGame);
//            userRepository.delete(playerOne);
//            userRepository.delete(playerTwo);
//        }
//        @Test
//        public void createBoardTest(){
//            List<Space> board = gameAdminService.createBoard();
//            Assert.assertEquals(25, board.size());
//            Assert.assertEquals(4, board.get(24).getCoordinate().getY());
//            Assert.assertEquals(4, board.get(24).getCoordinate().getX());
//        }
//        @Test
//        public void createSpaceTest(){
//            Space space = gameAdminService.createSpace(0,0);
//            Assert.assertEquals(0, space.getCoordinate().getX());
//            Assert.assertEquals(0, space.getCoordinate().getY());
//            Assert.assertNull(space.getWorkerId());
//            Assert.assertNull(space.getToplevelblock());
//            Assert.assertNotNull(space.getSpaceId());
//            Assert.assertFalse(space.getIsOccupied());
//        }
//        @Test
//        public void createInitialTurnTest(){
//            playerOne = userService.createUser(playerOne);
//            playerTwo = userService.createUser(playerTwo);
//            Game createdGame = gameAdminService.createGame(playerOne.getId(), playerOne, newGame);
//            Game joinedGame = gameAdminService.joinGame(playerTwo.getId(), createdGame.getGameId(), playerTwo);
//            gameAdminService.createInitialTurn(joinedGame,playerOne.getId());
//            Game gameWithFirstTurn = gameRepository.findByGameId(joinedGame.getGameId());
//            Assert.assertEquals(playerOne.getId(),gameWithFirstTurn.getCurrentTurn().getUserId());
//            Assert.assertNotNull(gameWithFirstTurn.getCurrentTurn());
//            Assert.assertEquals(joinedGame.getGameId(), gameWithFirstTurn.getCurrentTurn().getGameId());
//            gameRepository.delete(joinedGame);
//            userRepository.delete(playerOne);
//            userRepository.delete(playerTwo);
        }

