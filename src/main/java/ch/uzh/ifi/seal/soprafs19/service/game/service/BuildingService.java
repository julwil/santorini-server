package ch.uzh.ifi.seal.soprafs19.service.game.service;

import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.exceptions.GameRuleException;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Service
@Transactional
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final FigureService figureService;
    private final GameService gameService;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository, FigureService figureService, GameService gameService)
    {
        this.buildingRepository = buildingRepository;
        this.figureService = figureService;
        this.gameService = gameService;
    }

    /*
     * returns a list of all buildings on the board
     */
    public Iterable<Building> getAllBuildings(Game game)
    {
        return this.buildingRepository.findAllByGame(game);
    }

    /*
     * puts a building on the game board
     */
    public String postBuilding(Game game, Building newBuilding) throws GameRuleException
    {
        game = gameService.loadGame(game.getId());

        if (!game.isBuildAllowedByUserId(newBuilding.getOwnerId())) {
            throw new GameRuleException();
        }

        Figure figure = figureService.loadFigure(game.getLastActiveFigureId());

        if (!figure.getPossibleBuilds().contains(newBuilding.getPosition())) {
            throw new GameRuleException();
        }

        figure.build(newBuilding);

        return "buildings/" + newBuilding.getId().toString();
    }

    /*
     * returns a list of possible positions where a building can be placed
     */
    public Iterable<Position> getPossibleBuilds(Game game) {
        game = gameService.loadGame(game.getId());

        if (!game.isBuildAllowedByUserId(game.getCurrentTurn().getId())) {
            return new ArrayList<Position>();
        }

        Figure lastActiveFigure = figureService.loadFigure(game.getLastActiveFigureId());

        return lastActiveFigure.getPossibleBuilds();
    }
}
