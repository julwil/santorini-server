package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.rules.RuleService;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
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

    public String postGameBoardFigure(Game game, Figure figure)
    {
        figure.setGame(game);
        figureRepository.save(figure);
        gameService.setLastActiveFigureInGame(figure, game);


        return "figures/"  + figure.getId();
    }

    public String postGameBoardBuilding(Game game, Building building)
    {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, buildingRepository, gameBoard);
        building.setGame(game);
       // gameService.swapTurns(game);
        Boolean success = ruleService.buildingAtValidPosition(game, building);

        if (!success) {
            return "invalid.build";
        }

        buildingRepository.save(building);

        return "buildings/" + building.getId().toString();
    }

    public String putGameBoardFigure(Game game, Figure figure)
    {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, buildingRepository, gameBoard);
        return "Hello";

    }
}
