package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.exceptions.GameRuleException;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.rule.RuleService;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class BuildingService {

    private final FigureRepository figureRepository;
    private final BuildingRepository buildingRepository;
    private final GameService gameService;

    @Autowired
    public BuildingService(FigureRepository figureRepository, BuildingRepository buildingRepository, GameService gameService)
    {
        this.figureRepository = figureRepository;
        this.buildingRepository = buildingRepository;
        this.gameService = gameService;
    }

    /*
     * returns a list of all buildings on the board
     */
    public Iterable<Building> getGameBoardBuildings(Game game)
    {
        return this.buildingRepository.findAllByGame(game);
    }

    /*
     * puts a building on the game board
     */
    public String postGameBoardBuilding(Game game, Building building) throws GameRuleException
    {
        GameBoard gameBoard = new GameBoard(game, figureRepository, buildingRepository);
        RuleService ruleService = new RuleService(figureRepository, gameBoard);
        Boolean validPostBuilding = ruleService.postBuildingIsValid(game, building);

        if (!validPostBuilding) {
            throw new GameRuleException();
        }

        buildingRepository.save(building);
        //gameService.swapTurns(game);

        return "buildings/" + building.getId().toString();
    }
}
