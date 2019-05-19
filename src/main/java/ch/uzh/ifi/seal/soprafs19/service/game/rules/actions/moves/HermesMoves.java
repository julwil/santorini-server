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
import java.util.HashMap;


public class HermesMoves extends DefaultMoves {


    public HermesMoves(Figure figure, GameBoard board, BuildingRepository buildingRepository,
                       FigureRepository figureRepository,
                       GameRepository gameRepository, GameService gameService, FigureService figureService) {
        super(figure, board, buildingRepository, figureRepository,  gameRepository, gameService, figureService);

    }

    @Override
    public ArrayList<Position> calculatePossiblePositions() {

        int[] neighbourhood = {-1, 1, -1, 1, -3, 1};
        int[] neighbourhoodD = {-1, 1, -1, 1, 0, 0};// LowerX, UpperX, LowerY, UpperY, LowerZ, UpperZ
        ArrayList<Position> adjacentPositionsOfOrigin =  new ArrayList<>();

        HashMap<Integer, String> hmap= new HashMap<Integer, String>();


        adjacentPositionsOfOrigin.addAll(calculatePositionsInNeighbourhoodOfHermes(neighbourhoodD));


        // positions around origin
        int i = 0;
        while (i<14) {
            adjacentPositionsOfOrigin.addAll(calculatePossiblePositions2(adjacentPositionsOfOrigin));

            i++;
            removeDuplicates(adjacentPositionsOfOrigin);

        }

        adjacentPositionsOfOrigin.addAll(calculatePositionsInNeighbourhoodOfHermes(neighbourhood));
        adjacentPositionsOfOrigin.add(getOriginPosition());

        ArrayList<Position>cleanList =new ArrayList<>();

        cleanList=removeDuplicates(adjacentPositionsOfOrigin);


        return cleanList;
    }


    public ArrayList<Position> calculatePossiblePositions2(ArrayList<Position> candidates) {
        ArrayList<Position>tmp =new ArrayList<>();


        for (Position candidate: candidates )
        {tmp.addAll((calculatePositionsOfCandidates(candidate)));

        }
        stripOccupiedPositions(tmp);

        // Strip out the positions that are floating and have no building below
        stripFloatingPositions(tmp);

        return tmp;

    }




    public ArrayList<Position> calculatePositionsOfCandidates(Position position) {
        ArrayList<Position> adjacentPositions = new ArrayList<>();


        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                if (dx != 0 || dy != 0) {
                    if (dx == 0 && dy == 0) { // moving up/down along the z-axis ONLY is not allowed
                        continue;
                    }

                    Position tmp = new Position(
                            position.getX() + dx,
                            position.getY() + dy,
                            position.getZ()
                    );

                    if (tmp.hasValidAxis()) {
                        adjacentPositions.add(tmp);
                    }
                }
            }
        }
        stripOccupiedPositions(adjacentPositions);

        // Strip out the positions that are floating and have no building below
        stripFloatingPositions(adjacentPositions);



        return adjacentPositions;
    }



    public ArrayList<Position> calculatePositionsInNeighbourhoodOfHermes(int [] inBounds)
    {
        ArrayList<Position> adjacentPositions = new ArrayList<>();

        for (int dx = inBounds[0]; dx <= inBounds[1]; ++dx) {
            for (int dy = inBounds[2]; dy <= inBounds[3]; ++dy) {
                for (int dz = inBounds[4]; dz <= inBounds[5]; ++dz) {
                    if (dx != 0 || dy != 0 || dz != 0) {
                        if (dx == 0 && dy == 0) { // moving up/down along the z-axis ONLY is not allowed
                            continue;
                        }

                        Position tmp = new Position(
                                getOriginPosition().getX() + dx,
                                getOriginPosition().getY() + dy,
                                getOriginPosition().getZ() + dz
                        );

                        if (tmp.hasValidAxis()) {
                            adjacentPositions.add(tmp);
                        }
                    }
                }
            }

        }stripOccupiedPositions(adjacentPositions);

        // Strip out the positions that are floating and have no building below
        stripFloatingPositions(adjacentPositions);


        return adjacentPositions;
    }



    public ArrayList<Position> removeDuplicates(ArrayList<Position> list)
    {

        ArrayList<Position> newList = new ArrayList<Position>();

        for (Position element : list) {


            if (!newList.contains(element)) {

                newList.add(element);
            }
        }


        return newList;
    }


}