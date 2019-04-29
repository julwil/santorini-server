package ch.uzh.ifi.seal.soprafs19.rules;

import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.entity.BoardItem;
import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Map;

public class RuleService {

    private final FigureRepository figureRepository;
    private final BuildingRepository buildingRepository;
    private final GameBoard gameBoard;
    private ArrayList<String> validations = new ArrayList<>();

    public RuleService(FigureRepository figureRepository, BuildingRepository buildingRepository, GameBoard gameBoard)
    {
        this.figureRepository = figureRepository;
        this.buildingRepository = buildingRepository;
        this.gameBoard = gameBoard;
    }

    public  Boolean buildingAtValidPosition(Game game, Building building)
    {
        Position positionToBuild = building.getPosition();
        Figure lastActiveFigure = figureRepository.findById(game.getLastActiveFigureId());
        ArrayList<Position> adjPos = lastActiveFigure.getPosition().getAdjacentPositions();
        Map<Position, BoardItem> board = gameBoard.getBoardMap();
        adjPos.removeAll(board.values());

        // If position to build is not in the neighbourhood of the last active figure
        if (!adjPos.contains(positionToBuild)) {
            return false;
        }

        // If position to build is already occupied
        if (board.containsKey(positionToBuild)) {
            return false;
        }

        // If position is not at level 0
        if (!positionToBuild.isFloor()) {
            Position lower = positionToBuild;
            lower.setZ(lower.getZ() - 1);

            // The position below must not be empty
            if (!board.containsKey(lower)) {
                return false;
            }

            // You can't build upon a figure
            if (board.get(lower) instanceof Figure) {
                return false;
            }
        }

        return true;
    }
}
