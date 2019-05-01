package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.exceptions.GameRuleException;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.rule.RuleService;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        RuleService ruleService = new RuleService(figureRepository, gameBoard);
        Boolean validPostFigure = ruleService.postFigureIsValid(figure);

        if (!validPostFigure) {
            throw new GameRuleException();
        }

        figureRepository.save(figure);
        gameService.setLastActiveFigureInGame(figure, game);

        return "figures/" + figure.getId();
    }

    /*
     * moves a figure on the board to a target position
     */
    public String putGameBoardFigure(Game game, Figure figure, Position target) throws GameRuleException {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, gameBoard);
        Boolean validPutFigure = ruleService.putFigureIsValid(figure, target);

        if (!validPutFigure) {
            throw new GameRuleException();
        }

        figure.setPosition(target);
        figureRepository.save(figure);
        gameService.setLastActiveFigureInGame(figure, game);

        return "figures/"  + figure.getId();
    }

    /*
     * returns a list of possible positions where a given figure can move to.
     */
    public Iterable<Position> getGameBoardFigurePossibleMoves(Game game, Figure figure)
    {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, gameBoard);

        return ruleService.getPossiblePutFigurePositions(figure.getPosition());
    }
}
