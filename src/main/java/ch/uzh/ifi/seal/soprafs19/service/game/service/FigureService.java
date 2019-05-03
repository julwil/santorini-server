package ch.uzh.ifi.seal.soprafs19.service.game.service;
import ch.uzh.ifi.seal.soprafs19.entity.Figure;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.exceptions.GameRuleException;
import ch.uzh.ifi.seal.soprafs19.repository.BuildingRepository;
import ch.uzh.ifi.seal.soprafs19.repository.FigureRepository;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.Action;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.builds.DefaultBuilds;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.moves.DefaultMoves;
import ch.uzh.ifi.seal.soprafs19.service.game.rules.moves.InitialMoves;
import ch.uzh.ifi.seal.soprafs19.utilities.GameBoard;
import ch.uzh.ifi.seal.soprafs19.utilities.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FigureService {

    private final Logger log = LoggerFactory.getLogger(FigureService.class);
    private final FigureRepository figureRepository;
    private final BuildingRepository buildingRepository;
    private final GameService gameService;

    @Autowired
    public FigureService(FigureRepository figureRepository, BuildingRepository buildingRepository, GameService gameService)
    {
        this.figureRepository = figureRepository;
        this.buildingRepository = buildingRepository;
        this.gameService = gameService;
    }

    /*
     * returns a list of all figures on the board
     */
    public Iterable<Figure> getAllFigures(Game game)
    {
        return this.figureRepository.findAllByGame(game);
    }

    /*
     * places a new figure on the game board
     */
    public String postFigure(Game game, Figure figure) throws GameRuleException
    {
        GameBoard board = new GameBoard(game, figureRepository, buildingRepository);
        Position targetPosition = figure.getPosition();
        long ownerId = figure.getOwnerId();

        if (board.figureCountPerOwner(ownerId) > 1 ||
            !targetPosition.hasValidAxis() ||
            board.getBoardMap().containsKey(targetPosition)) {
            throw new GameRuleException();
        }

        figureRepository.save(figure);
        return "figures/" + figure.getId();
    }

    /*
     * moves a figure on the board to the destination position
     */
    public String putFigure(long figureId, Position destination) throws GameRuleException {
        Figure figure = loadFigure(figureId);

        if (!figure.getPossibleMoves().contains(destination)) {
            throw new GameRuleException();
        }

        figure.moveTo(destination);
        figureRepository.save(figure);

        return "figures/"  + figure.getId();
    }

    /*
     * returns a list of possible positions where a given figure can move to.
     */
    public Iterable<Position> getPossibleMoves(long figureId)
    {
        Figure figure = loadFigure(figureId);

        return figure.getPossibleMoves();
    }

    /*
     * returns a list of positions where a new figure can be placed
     */
    public Iterable<Position> getPossibleInitialMoves(Game game)
    {
        Figure figure = new Figure();
        GameBoard board = new GameBoard(game, figureRepository, buildingRepository);
        InitialMoves initMoves = new InitialMoves(figure, board);
        figure.setMoves(initMoves);

        return figure.getPossibleMoves();
    }

    public Figure loadFigure(long id)
    {
        Figure dbFigure = figureRepository.findById(id);
        GameBoard board = new GameBoard(dbFigure.getGame(), figureRepository, buildingRepository);

        Action moves = new DefaultMoves(dbFigure, board);
        Action builds = new DefaultBuilds(dbFigure, board);

        dbFigure.setMoves(moves);
        dbFigure.setBuilds(builds);

        return dbFigure;
    }
}
