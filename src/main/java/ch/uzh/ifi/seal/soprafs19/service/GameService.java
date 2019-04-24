package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.GameBoard;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceNotFoundException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.repository.GameBoardRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final GameBoardRepository gameBoardRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public GameService(
            GameRepository gameRepository,
            GameBoardRepository gameBoardRepository,
            UserRepository userRepository,
            UserService userService)
    {
        this.gameRepository = gameRepository;
        this.gameBoardRepository = gameBoardRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public String postCreateGame(Game newGame)
    {
        // Get the users by extracting the user id's from the game
        User user1 = newGame.getUser1();
        User user2 = newGame.getUser2();

        // Check if a users are offline
        if (!(userService.isOnline(user1) && userService.isOnline(user2))) {
            return "Both users have to be online and not involved in a game";
        }

        // Check if user1 and user2 are different
        if (user1.equals(user2)) {
            return "You can't play against yourself";
        }

        newGame.setStatus(GameStatus.INITIALIZED);
        newGame.setCurrentTurn(user2);
        gameRepository.save(newGame);

        user1.setGame(newGame);
        user1.setStatus(UserStatus.CHALLENGED);
        userRepository.save(user1);

        user2.setGame(newGame);
        user2.setStatus(UserStatus.CHALLENGED);
        userRepository.save(user2);

        return "games/" + newGame.getId().toString();
    }

    public Game postAcceptGameRequestByUser(long id, User acceptingUser) throws ResourceActionNotAllowedException, ResourceNotFoundException
    {
        try {
            Game game = gameRepository.findById(id);

            if (!game.getUser2().equals(acceptingUser)) {
                throw new ResourceActionNotAllowedException("Missing permission to accept the game");
            }
            GameBoard board = new GameBoard();
            board.setGame(game);
            gameBoardRepository.save(board);

            game.setStatus(GameStatus.STARTED);
            gameRepository.save(game);

            User user1 = game.getUser1();
            User user2 = game.getUser2();

            user1.setStatus(UserStatus.PLAYING);
            user2.setStatus(UserStatus.PLAYING);

            userRepository.save(user1);
            userRepository.save(user2);

            return game;
        }
        catch (NullPointerException e) {
            throw new ResourceNotFoundException("No game with matching id found");
        }
    }

    public void postCancelGameRequestByUser(long id, User cancelingUser) throws ResourceNotFoundException, ResourceActionNotAllowedException
    {
        try {
            Game game = gameRepository.findById(id);

            if (!(game.getUser1().equals(cancelingUser) || game.getUser2().equals(cancelingUser))) {
                throw new ResourceActionNotAllowedException("Missing permission to cancel the game");
            }
            game.setStatus(GameStatus.CANCLED);
            gameRepository.save(game);

            User user1 = game.getUser1();
            User user2 = game.getUser2();

            user1.setStatus(UserStatus.ONLINE);
            user2.setStatus(UserStatus.ONLINE);

            userRepository.save(user1);
            userRepository.save(user2);
        }
        catch (NullPointerException e) {
            throw new ResourceNotFoundException("No game with matching id found");
        }
    }

    public Iterable<Game> getAllGames(String token)
    {
        return gameRepository.findAll();
    }

    public Game getGameById(long id)
    {
        return gameRepository.findById(id);
    }

    public Iterable<Game> getGamesForUser2AndStatus(User user2, GameStatus status)
    {
        return gameRepository.findByUser2AndStatus(user2, status);
    }

    public GameBoard getGameBoardByGameId(long id) {
        return gameBoardRepository.findByGame(gameRepository.findById(id));
    }
}
