package ch.uzh.ifi.seal.soprafs19.service.game.rules.turn;
import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.exceptions.GameRuleException;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Turn {

    private final MoveRepository moveRepository;
    private final BuildingRepository buildingRepository;
    private final FigureRepository figureRepository;
    private final GameBoard board;

    @Autowired
    public Turn(GameBoard board, MoveRepository moveRepository, BuildingRepository buildingRepository, FigureRepository figureRepository) {
        this.moveRepository = moveRepository;
        this.buildingRepository = buildingRepository;
        this.figureRepository = figureRepository;
        this.board = board;
    }

    public abstract boolean isBuildingAllowed(Building newBuilding);

    public boolean isPlaceFigureAllowed(Figure figure) {
        return board.figureCountPerOwner(figure.getOwnerId()) < 2;
    }

    public abstract boolean isMoveAllowed(Figure figure);
}
