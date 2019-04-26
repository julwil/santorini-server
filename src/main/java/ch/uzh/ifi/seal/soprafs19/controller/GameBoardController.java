package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.service.GameBoardService;
import org.springframework.web.bind.annotation.*;


@RestController
public class GameBoardController {

    private final GameBoardService service;
    private final GameRepository gameRepository;

    GameBoardController(GameBoardService service, GameRepository gameRepository)
    {
        this.service = service;
        this.gameRepository = gameRepository;
    }

    @GetMapping(value = "/games/{id}/figures")
    public Iterable<Figure> getGameBoardFigures (
            @PathVariable long id,
            @RequestHeader("authorization") String token)
    {
        Game game = gameRepository.findById(id);
        return service.getGameBoardFigures(game);
    }

    @PostMapping(value = "/games/{id}/figures")
    public void postGameBoardFigure (
            @PathVariable long id,
            @RequestBody Figure figure
    )
    {
        Game game = gameRepository.findById(id);
        service.postGameBoardFigure(game, figure);
    }

    @GetMapping(value = "/games/{id}/buildings")
    public Iterable<Building> getGameBoardBuildings (@PathVariable long id)
    {
        Game game = gameRepository.findById(id);
        return service.getGameBoardBuildings(game);
    }

    @PostMapping(value = "/games/{id}/buildings")
    public void postGameBoardBuilding (
            @PathVariable long id,
            @RequestBody Building building
    )
    {
        Game game = gameRepository.findById(id);
        service.postGameBoardBuilding(game, building);
    }
}
