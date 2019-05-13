package ch.uzh.ifi.seal.soprafs19.service.game.service;

import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.exceptions.GameRuleException;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.actions.Action;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.actions.builds.DefaultBuilds;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.actions.moves.DefaultMoves;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.actions.moves.InitialMoves;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class FigureService {

    private final Logger log = LoggerFactory.getLogger(FigureService.class);
    private final FigureRepository figureRepository;
    private final BuildingRepository buildingRepository;
    private final MoveRepository moveRepository;
    private final GameRepository gameRepository;
    private final GameService gameService;

    @Autowired
    public FigureService(FigureRepository figureRepository, BuildingRepository buildingRepository, MoveRepository moveRepository, GameRepository gameRepository, GameService gameService)
    {
        this.figureRepository = figureRepository;
        this.buildingRepository = buildingRepository;
        this.moveRepository = moveRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
    }

    /*
     * returns a list of all figures on the board
     */
    public Iterable<Figure> getAllFigures(Game game)
    {
        return this.figureRepository.findAllByGame(game);
    }

    /*
     * places a new figure on the game board
     */
    public String postFigure(Game game, Figure figure) throws GameRuleException
    {
        game = gameService.loadGame(game.getId());
        GameBoard board = new GameBoard(game, figureRepository, buildingRepository);
        Action moves = new InitialMoves(figure, board, buildingRepository, figureRepository, moveRepository, gameRepository, gameService, this);
        figure.setMoves(moves);
        long ownerId = figure.getOwnerId();

        if (!game.isPlaceFigureAllowedByUserId(ownerId)) {
            throw new GameRuleException();
        }

        Position targetPosition = figure.getPosition();

        if (!targetPosition.hasValidAxis() ||
            !figure.getPossibleMoves().contains(targetPosition)) {
            throw new GameRuleException();
        }

        figure.moveTo(targetPosition);

        return "figures/" + figure.getId();
    }

    /*
     * moves a figure on the board to the destination position
     */
    public String putFigure(long figureId, Position destination) throws GameRuleException
    {
        Figure figure = loadFigure(figureId);
        Game game = gameService.loadGame(figure.getGame().getId());

        if (!game.isMoveAllowedByUserId(figure.getOwnerId()) || !figure.getPossibleMoves().contains(destination)) {
            throw new GameRuleException();
        }

        figure.moveTo(destination);

        return "figures/"  + figure.getId();
    }

    /*
     * returns a list of possible positions where a given figure can move to.
     */
    public Iterable<Position> getPossibleMoves(long figureId)
    {
        Figure figure = loadFigure(figureId);
        Game game = gameService.loadGame(figure.getGame().getId());

        if (!game.isMoveAllowedByUserId(figure.getOwnerId())) {
            return new ArrayList<Position>();
        }

        return figure.getPossibleMoves();
    }

    /*
     * returns a list of positions where a new figure can be placed
     */
    public Iterable<Position> getPossibleInitialMoves(Game game)
    {
        game = gameService.loadGame(game.getId());

        // If the user is not allowed to place figures, return empty list.
        if (!game.isPlaceFigureAllowedByUserId(game.getCurrentTurn().getId())) {
            return new ArrayList<Position>();
        }

        Figure figure = loadInitialFigure(game);

        return figure.getPossibleMoves();
    }

    public Figure loadFigure(long id)
    {
        Figure dbFigure = figureRepository.findById(id);
        GameBoard board = new GameBoard(dbFigure.getGame(), figureRepository, buildingRepository);

        Action moves = new DefaultMoves(dbFigure, board, buildingRepository, figureRepository, moveRepository, gameRepository, gameService, this);
        Action builds = new DefaultBuilds(dbFigure, board, buildingRepository, figureRepository, moveRepository, gameRepository, gameService, this);

        dbFigure.setMoves(moves);
        dbFigure.setBuilds(builds);

        return dbFigure;
    }

    private Figure loadInitialFigure(Game game)
    {
        Figure figure = new Figure();
        GameBoard board = new GameBoard(game, figureRepository, buildingRepository);
        InitialMoves initMoves = new InitialMoves(figure, board, buildingRepository, figureRepository, moveRepository, gameRepository, gameService, this);
        figure.setMoves(initMoves);

        return figure;
    }
}
