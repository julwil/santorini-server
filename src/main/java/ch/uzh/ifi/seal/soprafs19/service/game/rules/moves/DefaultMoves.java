package ch.uzh.ifi.seal.soprafs19.service.game.rules.moves;

import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.Action;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import java.util.ArrayList;

public class DefaultMoves extends Action {

    public DefaultMoves(Figure figure, GameBoard board)
    {
        super(figure, board);
    }

    @Override
    public ArrayList<Position> calculatePossiblePositions()
    {
        int [] neighbourhood = {-1, 1, -1, 1, -3, 1}; // LowerX, UpperX, LowerY, UpperY, LowerZ, UpperZ
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
        getFigure().setPosition(getTargetPosition());
    }
}
