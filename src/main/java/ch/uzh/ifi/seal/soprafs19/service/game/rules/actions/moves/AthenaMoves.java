package ch.uzh.ifi.seal.soprafs19.service.game.rules.actions.moves;

import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.actions.Action;
import ch.uzh.ifi.seal.soprafs19.service.game.service.FigureService;
import ch.uzh.ifi.seal.soprafs19.service.game.service.GameService;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;

import java.util.ArrayList;

public class AthenaMoves extends DefaultMoves {

    public AthenaMoves(Figure figure, GameBoard board, BuildingRepository buildingRepository,
                       FigureRepository figureRepository, MoveRepository moveRepository,
                       GameRepository gameRepository, GameService gameService, FigureService figureService) {
        super(figure, board, buildingRepository, figureRepository, moveRepository, gameRepository, gameService, figureService);
    }

    @Override
    public ArrayList<Position> calculatePossiblePositions() {
        int[] neighbourhood = {-1, 1, -1, 1, -3, 1};

        if (game.getCurrentTurn().getId() != game.checkForAthena().getId() &&(game.statusAthenaMovedUp() == 1)){
            int[] newNeighbourhood = {-1, 1, -1, 1, -3, 0};
            ArrayList<Position> adjacentPositionsOfOrigin = calculatePositionsInNeighbourhood(newNeighbourhood);

            // Strip out positions that are occupied by other board items
            stripOccupiedPositions(adjacentPositionsOfOrigin);

            // Strip out the positions that are floating and have no building below
            stripFloatingPositions(adjacentPositionsOfOrigin);


            return adjacentPositionsOfOrigin;
        }

        ArrayList<Position> adjacentPositionsOfOrigin = calculatePositionsInNeighbourhood(neighbourhood);

        // Strip out positions that are occupied by other board items
        stripOccupiedPositions(adjacentPositionsOfOrigin);

        // Strip out the positions that are floating and have no building below
        stripFloatingPositions(adjacentPositionsOfOrigin);

        return adjacentPositionsOfOrigin;

    }

    @Override
    public void perform()
    {
        if (game.getCurrentTurn().getId()==game.checkForAthena().getId() && getTargetPosition().getZ() - getOriginPosition().getZ() == 1) {
            game.activateAthenaMovedUp();

        }
        else{
            game.deactivateAthenaMovedUp();
        }

        getFigure().setPosition(getTargetPosition());
        figureRepository.save(getFigure());

        game.setLastActiveFigureId(getFigure().getId());
        gameRepository.save(game);

        handleWindCondition();
    }

}