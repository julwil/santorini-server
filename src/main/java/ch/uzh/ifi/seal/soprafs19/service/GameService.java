package ch.uzh.ifi.seal.soprafs19.service;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;

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

    public Game getGameById(long id) {
        return null;
    }


    public String createGame(Game game) {
        return "3";
    }

    public Iterable<Game> getAllGames(String token)throws FailedAuthenticationException  {
        if ( this.userRepository.existsByToken(token)) {
            return gameRepository.findAll();

        } else {
            throw new FailedAuthenticationException();
        }
    }
}






