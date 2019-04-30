package ch.uzh.ifi.seal.soprafs19.rule;

import ch.uzh.ifi.seal.soprafs19.constant.Axis;
import ch.uzh.ifi.seal.soprafs19.entity.BoardItem;
import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;

import java.util.ArrayList;
import java.util.Iterator;
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

    /*
     * returns whether the place action of a figure is valid
     */
    public Boolean postFigureIsValid(Game game, Figure figure)
    {
        ArrayList<Figure> figuresByGameAndOwner = (ArrayList<Figure>) figureRepository.findAllByGameAndOwnerId(game, figure.getOwnerId());
        Position positionToPlaceFigure = figure.getPosition();
        Boolean notYetTwoFiguresPosted = figuresByGameAndOwner.size() < 2;
        ArrayList<Position> possiblePostFigurePositions = getPossiblePostFigurePositions();

        return notYetTwoFiguresPosted &&
               possiblePostFigurePositions.contains(positionToPlaceFigure);
    }

    /*
     * returns whether the build action is valid
     */
    public  Boolean postBuildingIsValid(Game game, Building buildingToBuild)
    {
        Position positionToBuild = buildingToBuild.getPosition();
        Figure lastActiveFigure = figureRepository.findById(game.getLastActiveFigureId());
        ArrayList<Position> possibleBuildPositions = getPossiblePostBuildingPositions(lastActiveFigure.getPosition());

        // Don't build on an invalid position
        return possibleBuildPositions.contains(positionToBuild);
    }

    /*
     * returns whether the move action is valid
     */
    public Boolean putFigureIsValid(Game game, Figure figureToMove, Position targetPosition)
    {
        Position originPosition = figureToMove.getPosition();
        ArrayList<Position> possibleMoveFigurePositions = getPossiblePutFigurePositions(originPosition);

        return possibleMoveFigurePositions.contains(targetPosition);
    }

    /*
     * returns a list of possible positions where the the figure can move to based on an origin position
     */
    private ArrayList<Position> getPossiblePutFigurePositions(Position origin)
    {
        // Get all adjacentPositions
        ArrayList<Position> adjacentPositions = new ArrayList<>();

        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -2; dz <= 1; ++dz) {  // !!
                    if (dx != 0 || dy != 0 || dz != 0) {
                        if (dx == 0 && dy == 0) { // moving up/down without changing x /y is not allowed
                            continue;
                        }
                        Position tmp = new Position(
                                origin.getX() + dx,
                                origin.getY() + dy,
                                origin.getZ() + dz);

                        if (validPosition(tmp)) {
                            adjacentPositions.add(tmp);
                        }}}}}

        // Only consider unoccupied positions
        stripOccupiedPositions(adjacentPositions);

        // Strip out the positions that are floating and have no building below
        stripFloatingPositions(adjacentPositions);

        return adjacentPositions;
    }

    /*
     * returns a list of possible positions where a building can be placed,
     * based on the position of the last active figure.
     */

    private ArrayList<Position> getPossiblePostBuildingPositions(Position positionOfLastActiveFigure)
    {
        // Get all adjacentPositions
        ArrayList<Position> adjacentPositions = new ArrayList<>();

        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -2; dz <= 3; ++dz) { // !!
                    if (dx != 0 || dy != 0 || dz != 0) {
                        if (dx == 0 && dy == 0) { // moving up/down without changing x /y is not allowed
                            continue;
                        }
                        Position tmp = new Position(
                                positionOfLastActiveFigure.getX() + dx,
                                positionOfLastActiveFigure.getY() + dy,
                                positionOfLastActiveFigure.getZ() + dz);

                        if (validPosition(tmp)) {
                            adjacentPositions.add(tmp);
                        }}}}}

        // Only consider unoccupied positions
        stripOccupiedPositions(adjacentPositions);

        // Strip out the positions that are floating and have no building below
        stripFloatingPositions(adjacentPositions);

        return adjacentPositions;
    }

    /*
     * returns a list of possible positions where a figure can be placed on the board
     */

    private ArrayList<Position> getPossiblePostFigurePositions()
    {
        ArrayList<Position> possiblePositions = new ArrayList<>();
        Map<Position, BoardItem> board = gameBoard.getBoardMap();

        // Calculate all possible positions on the 0th level
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Position possiblePosition = new Position(x, y, 0);
                possiblePositions.add(possiblePosition);
            }
        }

        // Remove all positions which are already occupied.
        possiblePositions.removeAll(board.keySet());

        return possiblePositions;
    }




    // Helpers:

    /*
     * deletes all positions which don't are floor positions or don't have a building below them
     */
    private void stripFloatingPositions(ArrayList<Position> adjacentPositions)
    {
        // For all positions higher than level 0 z in {1,2,3} check if the field below has a building, else remove the original field
        for (Iterator<Position> iterator = adjacentPositions.iterator(); iterator.hasNext();) {
            Position adjacentPosition = iterator.next();
            if (adjacentPosition.isFloor()) {
                continue;
            }

            Position lowerPosition = new Position(
                    adjacentPosition.getX(),
                    adjacentPosition.getY(),
                 adjacentPosition.getZ() -1
            );

            // If there is no board item below the current position,
            // the position is considered floating and must be removed.
            if (!gameBoard.getBoardMap().containsKey(lowerPosition)) {
                iterator.remove();
                continue;
            }

            // If there is a board item below the position and it is a figure,
            // the position is invalid since you can't build upon figures and must be removed.
            BoardItem itemAtLowerPosition = gameBoard.getBoardMap().get(lowerPosition);
            if (itemAtLowerPosition instanceof Figure) {
                iterator.remove();
            }
        }
    }

    /*
     * deletes all positions which are already occupied by a board item
     */
    private void stripOccupiedPositions(ArrayList<Position> adjacentPositions)
    {
        Map<Position, BoardItem> board = gameBoard.getBoardMap();
        adjacentPositions.removeAll(board.keySet());
    }

    /*
     * returns whether the position is valid according to the board dimensions
     */
    private boolean validPosition(Position position)
    {
        return Axis.XYAXIS.contains(position.getX()) &&
                Axis.XYAXIS.contains(position.getY()) &&
                Axis.ZAXIS.contains(position.getZ());
    }
}
