package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.entity.Building;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;
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

    public Iterable<Figure> getGameBoardFigures(Game game)
    {
        Position position = new Position(2,1,0);
        Figure figure = new Figure();
        figure.setPosition(position);
        figure.setGame(game);
        figureRepository.save(figure);

        return this.figureRepository.findAllByGame(game);
    }

    public Iterable<Building> getGameBoardBuildings(Game game)
    {
        Position position = new Position(2,1,0);
        Building building = new Building();
        building.setPosition(position);
        building.setGame(game);
        buildingRepository.save(building);

        return this.buildingRepository.findAllByGame(game);
    }

    public void postGameBoardFigure(Game game, Figure figure) 
    {
        figureRepository.save(figure);
    }

    public void postGameBoardBuilding(Game game, Building building)
    {
        buildingRepository.save(building);
    }

}
