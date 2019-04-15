package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public GameService(GameRepository gameRepository, UserService userService, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public String createGame(Game newGame) {
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

    public Iterable<Game> getAllGames(String token) {
        return gameRepository.findAll();
    }
    public Game getGameById(long id){
        return gameRepository.findById(id);
    }
    public Iterable<Game> getGamesForUser2(User user2) { return gameRepository.findByUser2(user2); }
}
