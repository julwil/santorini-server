package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.entity.Building;
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
public class GameBoardService {

    private final Logger log = LoggerFactory.getLogger(GameBoardService.class);
    private final FigureRepository figureRepository;
    private final BuildingRepository buildingRepository;
    private final GameService gameService;
    //private final GameBoard gameBoard;

    @Autowired
    public GameBoardService(FigureRepository figureRepository, BuildingRepository buildingRepository, GameService gameService)
    {
        this.figureRepository = figureRepository;
        this.buildingRepository = buildingRepository;
        this.gameService = gameService;
        //this.gameBoard = gameBoard;
    }

    public Iterable<Figure> getGameBoardFigures(Game game)
    {
        return this.figureRepository.findAllByGame(game);
    }

    public Iterable<Building> getGameBoardBuildings(Game game)
    {
        return this.buildingRepository.findAllByGame(game);
    }

    public String postGameBoardFigure(Game game, Figure figure) throws GameRuleException {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, buildingRepository, gameBoard);
        Boolean validPostFigure = ruleService.postFigureIsValid(game, figure);

        if (!validPostFigure) {
            throw new GameRuleException();
        }

        figureRepository.save(figure);
        gameService.setLastActiveFigureInGame(figure, game);

        return "figures/"  + figure.getId();
    }

    public String postGameBoardBuilding(Game game, Building building) throws GameRuleException {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, buildingRepository, gameBoard);
        Boolean validPostBuilding = ruleService.postBuildingIsValid(game, building);

        if (!validPostBuilding) {
            throw new GameRuleException();
        }

        buildingRepository.save(building);
        //gameService.swapTurns(game);

        return "buildings/" + building.getId().toString();
    }

    public String putGameBoardFigure(Game game, Figure figure, Position target) throws GameRuleException {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, buildingRepository, gameBoard);
        Boolean validPutFigure = ruleService.putFigureIsValid(game, figure, target);

        if (!validPutFigure) {
            throw new GameRuleException();
        }

        figure.setPosition(target);
        figureRepository.save(figure);
        gameService.setLastActiveFigureInGame(figure, game);

        return "figures/"  + figure.getId();
    }
}
