package ch.uzh.ifi.seal.soprafs19.service.game.service;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.exceptions.GameRuleException;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.rule.RuleService;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.Action;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.builds.DefaultBuilds;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.moves.DefaultMoves;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;


@Service
@Transactional
public class FigureService {

    private final Logger log = LoggerFactory.getLogger(FigureService.class);
    private final FigureRepository figureRepository;
    private final BuildingRepository buildingRepository;
    private final GameService gameService;

    @Autowired
    public FigureService(FigureRepository figureRepository, BuildingRepository buildingRepository, GameService gameService)
    {
        this.figureRepository = figureRepository;
        this.buildingRepository = buildingRepository;
        this.gameService = gameService;
    }

    /*
     * returns a list of all figures on the board
     */
    public Iterable<Figure> getGameBoardFigures(Game game)
    {
        return this.figureRepository.findAllByGame(game);
    }

    /*
     * places a new figure on the game board
     */
    public String postGameBoardFigure(Game game, Figure figure) throws GameRuleException {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, buildingRepository, gameBoard);
        Boolean validPostFigure = ruleService.postFigureIsValid(figure);

        if (!validPostFigure) {
            throw new GameRuleException();
        }

        figureRepository.save(figure);

        if (ruleService.getUserPostedTwoFigures()) {
            gameService.swapTurns(game);
        }

        Action moves = new DefaultMoves(figure, gameBoard);
        Action builds = new DefaultBuilds(figure, gameBoard);
        figure.setMoves(moves);
        figure.setBuilds(builds);
        figureRepository.save(figure);
        return "figures/" + figure.getId();
    }

/*    *//*
     * moves a figure on the board to a target position
     *//*
    public String putGameBoardFigure(Game game, Figure figure, Position target) throws GameRuleException {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, buildingRepository, gameBoard);
        Boolean validPutFigure = ruleService.putFigureIsValid(figure, target);

        if (!validPutFigure) {
            throw new GameRuleException();
        }

        figure.setPosition(target);
        figureRepository.save(figure);
        gameService.setLastActiveFigureInGame(figure, game);

        if (target.isCeil()) {
            //gameService.setWinner(figure.getGame(), figure.getGame().getCurrentTurn());
        }

        return "figures/"  + figure.getId();
    }*/

    public String putFigure(long figureId, Position destination) throws GameRuleException {
        Figure figure = loadFigure(figureId);

        if (!figure.getPossibleMoves().contains(destination)) {
            throw new GameRuleException();
        }

        figure.moveTo(destination);
        figureRepository.save(figure);

        return "figures/"  + figure.getId();
    }

    /*
     * returns a list of possible positions where a given figure can move to.
     */
    public Iterable<Position> getGameBoardFigurePossiblePuts(long figureId)
    {
        Figure figure = loadFigure(figureId);

        return figure.getPossibleMoves();
    }

    public Iterable<Position> getGameBoardFigurePossiblePosts(Game game) {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, buildingRepository, gameBoard);

        return ruleService.getPossiblePostFigurePositions();
    }

    public Figure loadFigure(long id)
    {
        Figure dbFigure = figureRepository.findById(id);
        GameBoard board = new GameBoard(dbFigure.getGame(), figureRepository, buildingRepository);

        Action moves = new DefaultMoves(dbFigure, board);
        Action builds = new DefaultBuilds(dbFigure, board);

        dbFigure.setMoves(moves);
        dbFigure.setBuilds(builds);

        return dbFigure;
    }
}
