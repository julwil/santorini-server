package ch.uzh.ifi.seal.soprafs19.service.game.rules.turn;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;

public class DefaultTurn extends Turn {


    public DefaultTurn(GameBoard board, MoveRepository moveRepository, BuildingRepository buildingRepository, FigureRepository figureRepository, GameRepository gameRepository) {
        super(board, moveRepository, buildingRepository, figureRepository, gameRepository);
    }

    @Override
    public boolean isBuildAllowedByUserId(long userId) {
        return isCurrentTurn(userId);
    }

    @Override
    public boolean isMoveAllowedByUserId(long userId) {
        return isCurrentTurn(userId);
    }
}
