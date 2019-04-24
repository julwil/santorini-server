package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class GameBoardService {

    private final Logger log = LoggerFactory.getLogger(GameBoardService.class);
    private final FigureRepository figureRepository;
    private final BuildingRepository buildingRepository;

    @Autowired
    public GameBoardService(FigureRepository figureRepository, BuildingRepository buildingRepository)
    {
        this.figureRepository = figureRepository;
        this.buildingRepository = buildingRepository;
    }

    public GameBoard getGameBoardByGame(Game game)
    {
        return new GameBoard(game, figureRepository, buildingRepository);
    }
}
