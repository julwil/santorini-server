package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.service.GameBoardService;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
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

    // Get the board of a game
    @GetMapping(value = "/games/{id}/board")
    public GameBoard getGameBoard (@PathVariable long id)
    {
        Game game = gameRepository.findById(id);
        return service.getGameBoardByGame(game);
    }
}
