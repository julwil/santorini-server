package ch.uzh.ifi.seal.soprafs19.service.game.rules.actions.moves;

import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.service.game.service.FigureService;
import ch.uzh.ifi.seal.soprafs19.service.game.service.GameService;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;

import java.util.ArrayList;


public class ArtemisMoves extends DefaultMoves {
    public ArtemisMoves(Figure figure, GameBoard board, BuildingRepository buildingRepository,
                        FigureRepository figureRepository,
                        GameRepository gameRepository, GameService gameService, FigureService figureService)
    {
        super(figure, board, buildingRepository, figureRepository, gameRepository, gameService, figureService);
    }

    public ArrayList<Position> calculatePossiblePositionsArtemis() {

        Position originalPosition = getOriginPosition();

         // LowerX, UpperX, LowerY, UpperY, LowerZ, UpperZ
        ArrayList<Position> adjacentPositionsOfOrigin = calculatePossiblePositions();

        ArrayList<Position> tmpAdjacent = (ArrayList<Position>) adjacentPositionsOfOrigin.clone();

        for (int i = 0; i < tmpAdjacent.size(); i++) {

            Position tempPosition = tmpAdjacent.get(i);

            for (int dx = -1; dx <= 1; ++dx) {
                for (int dy = -1; dy <= 1; ++dy) {
                    for (int dz = -3; dz <= 1; ++dz) {
                        if (dx != 0 || dy != 0 || dz != 0) {
                            if (dx == 0 && dy == 0) { // moving up/down along the z-axis ONLY is not allowed
                                continue;
                            }

                            Position tmp = new Position(
                                    tempPosition.getX() + dx,
                                    tempPosition.getY() + dy,
                                    tempPosition.getZ() + dz
                            );

                            if (tmp.hasValidAxis()) {
                                adjacentPositionsOfOrigin.add(tmp);
                            }
                        }
                    }
                }
            }


            // Strip out positions that are occupied by other board items
            stripOccupiedPositions(adjacentPositionsOfOrigin);

            // Strip out the positions that are floating and have no building below
            stripFloatingPositions(adjacentPositionsOfOrigin);

            adjacentPositionsOfOrigin.remove(originalPosition);

        }
        return adjacentPositionsOfOrigin;
    }


}


