package ch.uzh.ifi.seal.soprafs19.service.game.rules.actions.moves;

import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.actions.Action;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;

import java.util.ArrayList;

public class InitialMoves extends Action {

    public InitialMoves(Figure figure, GameBoard board)
    {
        super(figure, board);
    }

    @Override
    public ArrayList<Position> calculatePossiblePositions()
    {
        ArrayList<Position> possiblePositions = new ArrayList<>();

        // Calculate all possible positions on the 0th level
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Position possiblePosition = new Position(x, y, 0);
                possiblePositions.add(possiblePosition);
            }
        }

        // Remove all positions which are already occupied.
        stripOccupiedPositions(possiblePositions);

        return possiblePositions;
    }

    @Override
    public void perform()
    {
        getFigure().setPosition(getTargetPosition());
    }
}
