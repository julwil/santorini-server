package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.exceptions.GameRuleException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceNotFoundException;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.GameBoardService;
import ch.uzh.ifi.seal.soprafs19.utilities.AuthenticationService;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
public class GameBoardController {

    private final GameBoardService service;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final FigureRepository figureRepository;
    private final AuthenticationService authenticationService;

    GameBoardController(GameBoardService service, GameRepository gameRepository, UserRepository userRepository, AuthenticationService authenticationService, FigureRepository figureRepository)
    {
        this.service = service;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.figureRepository = figureRepository;
        this.authenticationService = authenticationService;
    }

    @GetMapping(value = "/games/{id}/figures")
    public Iterable<Figure> getGameBoardFigures (
            @PathVariable long id,
            @RequestHeader("authorization") String token)
    {
        Game game = gameRepository.findById(id);
        return service.getGameBoardFigures(game);
    }

    @GetMapping(value = "/games/{id}/buildings")
    public Iterable<Building> getGameBoardBuildings (@PathVariable long id)
    {
        Game game = gameRepository.findById(id);
        return service.getGameBoardBuildings(game);
    }

    @PostMapping(value = "/games/{id}/figures")
    public Map<String, String> postGameBoardFigure (
            @RequestHeader("authorization") String token,
            @PathVariable long id,
            @RequestBody Position position,
            HttpServletResponse response)
            throws FailedAuthenticationException, ResourceNotFoundException, ResourceActionNotAllowedException, GameRuleException {
        authenticationService.authenticateUser(token);
        authenticationService.userTokenInGameById(token, id);
        authenticationService.userTokenIsCurrentTurn(token, id);

        Game game = gameRepository.findById(id);
        User user = userRepository.findByToken(token);
        Figure figure = new Figure();

        figure.setPosition(position);
        figure.setOwnerId(user.getId());
        figure.setGame(game);
        response.setStatus(201);

        HashMap<String, String> pathToFigure = new HashMap<>();
        pathToFigure.put("path", service.postGameBoardFigure(game, figure));

        return pathToFigure;
    }

    @PutMapping(value = "/games/{gameId}/figures/{figureId}")
    public Map<String, String> putGameBoardFigure (
            @RequestHeader("authorization") String token,
            @PathVariable long gameId,
            @PathVariable long figureId,
            @RequestBody Position position,
            HttpServletResponse response)
            throws FailedAuthenticationException, ResourceNotFoundException, ResourceActionNotAllowedException, GameRuleException {
        authenticationService.authenticateUser(token);
        authenticationService.userTokenInGameById(token, gameId);
        authenticationService.userTokenIsCurrentTurn(token,gameId);

        Game game = gameRepository.findById(gameId);
        User user = userRepository.findByToken(token);
        Figure figure = figureRepository.findById(figureId);

        response.setStatus(200);

        HashMap<String, String> pathToFigure = new HashMap<>();
        pathToFigure.put("path", service.putGameBoardFigure(game, figure, position));

        return pathToFigure;
    }

    @PostMapping(value = "/games/{id}/buildings")
    public Map<String, String> postGameBoardBuilding (
            @RequestHeader("authorization") String token,
            @PathVariable long id,
            @RequestBody Position position,
            HttpServletResponse response)
            throws FailedAuthenticationException, ResourceNotFoundException, ResourceActionNotAllowedException, GameRuleException {
        authenticationService.authenticateUser(token);
        authenticationService.userTokenInGameById(token, id);
        authenticationService.userTokenIsCurrentTurn(token, id);

        Game game = gameRepository.findById(id);
        User user = userRepository.findByToken(token);
        Building building = new Building();

        building.setPosition(position);
        building.setOwnerId(user.getId());
        building.setGame(game);
        response.setStatus(201);

        HashMap<String, String> pathToBuilding = new HashMap<>();
        pathToBuilding.put("path", service.postGameBoardBuilding(game, building));

        return pathToBuilding;
    }


}
