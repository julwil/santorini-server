package ch.uzh.ifi.seal.soprafs19.service.game.rules.turn;
import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;

public class DefaultTurn extends Turn {


    public DefaultTurn(GameBoard board, MoveRepository moveRepository, BuildingRepository buildingRepository, FigureRepository figureRepository) {
        super(board, moveRepository, buildingRepository, figureRepository);
    }

    @Override
    public boolean isBuildingAllowed(Building newBuilding) {
        return true;
    }

    @Override
    public boolean isMoveAllowed(Figure figure) {
        return false;
    }
}
