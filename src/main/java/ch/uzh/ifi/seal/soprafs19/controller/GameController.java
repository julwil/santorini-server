package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.exceptions.GameRuleException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceNotFoundException;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.GameServiceDemo;
import ch.uzh.ifi.seal.soprafs19.service.game.service.GameService;
import ch.uzh.ifi.seal.soprafs19.utilities.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
@RestController
public class GameController {

    private final GameService service;
    private final UserRepository userRepository;
    private final GameServiceDemo gameServiceDemo;
    private final AuthenticationService authenticationService;

    GameController(GameService service, UserRepository userRepository, GameServiceDemo gameServiceDemo, AuthenticationService authenticationService) {
        this.service = service;
        this.userRepository = userRepository;

        this.gameServiceDemo = gameServiceDemo;
        this.authenticationService = authenticationService;
    }

    // Create new Game
    @PostMapping(value = "/games",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> postCreateGame (
            @RequestHeader("authorization") String token,
            @Valid @RequestBody Game newGame,
            HttpServletResponse response) throws FailedAuthenticationException
    {
        authenticationService.authenticateUser(token);

        HashMap<String, String> pathToGame = new HashMap<>();
        pathToGame.put("path", this.service.postCreateGame(newGame));

        response.setStatus(201);
        return pathToGame;
    }

    @PostMapping("/games/{id}/accept")
    public Game postAcceptGameRequestByUser (
            @RequestHeader("authorization") String token,
            @PathVariable("id") long gameId,
            @RequestBody HashMap<String, Object> requestBody) throws ResourceNotFoundException, ResourceActionNotAllowedException, FailedAuthenticationException
    {
        authenticationService.authenticateUser(token);
        authenticationService.userTokenInGameById(token, gameId);

        User user = this.userRepository.findByToken(token);

        String selectedGodPower = "";
        if (requestBody.containsKey("selectedGodPower")) {
            selectedGodPower = (String) requestBody.get("selectedGodPower");
        }

        return service.postAcceptGameRequestByUser(gameId, user, selectedGodPower);
    }

    @PostMapping("/games/{id}/reject")
    void postCancelGameRequest (
            @RequestHeader("authorization") String token,
            @PathVariable("id") long gameId,
            HttpServletResponse response) throws ResourceNotFoundException, ResourceActionNotAllowedException, FailedAuthenticationException
    {

        authenticationService.authenticateUser(token);
        authenticationService.userTokenInGameById(token, gameId);

        User user = this.userRepository.findByToken(token);

        service.postCancelGameRequestByUser(gameId, user);
    }

    // Create new DemoGame
    @PostMapping(value = "/games/demoXWins",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> postCreateGameDemoXWins (
            @RequestHeader("authorization") String token,
            @Valid @RequestBody Game newGame,
            HttpServletResponse response) throws FailedAuthenticationException
    {

        authenticationService.authenticateUser(token);

        HashMap<String, String> pathToGame = new HashMap<>();
        pathToGame.put("id", this.gameServiceDemo.postCreateGameDemoXWins(newGame));

        response.setStatus(201);
        return pathToGame;
    }

        // Accept demo
        @PostMapping("/games/demo/{id}/accept")
    public Game postAcceptDemoGameRequestByUser (
            @RequestHeader("authorization") String token,
            @PathVariable("id") long gameId) throws GameRuleException, FailedAuthenticationException
    {
        authenticationService.authenticateUser(token);
        User user = this.userRepository.findByToken(token);

        return gameServiceDemo.postAcceptDemoGameRequestByUser(gameId, user);
    }

    @GetMapping(value = "/games/{id}",produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public Game getGameById (
            @RequestHeader("authorization") String token,
            @PathVariable(value="id") long id) throws FailedAuthenticationException
    {
        authenticationService.authenticateUser(token);
        return service.getGameById(id);
    }

    // Fetch all games
    @GetMapping("/games")
    public Iterable<Game> getAllGames (
            @RequestHeader("authorization") String token) throws FailedAuthenticationException
    {
        authenticationService.authenticateUser(token);
        return service.getAllGames(token);
    }

    // Fetch all games of the logged in user
    @GetMapping("/games/invitations")
    public Iterable<Game> getGamesForUser2 (
            @RequestHeader("authorization") String token) throws FailedAuthenticationException
    {
        authenticationService.authenticateUser(token);
        User user2 = this.userRepository.findByToken(token);
        return service.getGamesForUser2AndStatus(user2, GameStatus.INITIALIZED);
    }

    @PostMapping("/games/{id}/finishTurn")
    void postFinishTurn (
            @RequestHeader("authorization") String token,
            @PathVariable("id") long gameId,
            HttpServletResponse response) throws ResourceActionNotAllowedException, GameRuleException, FailedAuthenticationException
    {
        authenticationService.authenticateUser(token);
        authenticationService.userTokenIsCurrentTurn(token, gameId);
        Game game = this.service.getGameById(gameId);
        User user = this.userRepository.findByToken(token);


        this.service.postFinishTurn(game, user);

        response.setStatus(204);
    }
}
