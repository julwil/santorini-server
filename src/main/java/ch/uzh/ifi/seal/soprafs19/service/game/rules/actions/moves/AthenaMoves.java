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

public class AthenaMoves extends DefaultMoves {

    public AthenaMoves(Figure figure, GameBoard board, BuildingRepository buildingRepository,
                       FigureRepository figureRepository,
                       GameRepository gameRepository, GameService gameService, FigureService figureService) {
        super(figure, board, buildingRepository, figureRepository,  gameRepository, gameService, figureService);
    }




    @Override
    public void perform()
    {
        if (getTargetPosition().getZ() - getOriginPosition().getZ() == 1) {
            game.activateAthenaMovedUp();
            gameRepository.save(game);
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