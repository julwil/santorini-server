package ch.uzh.ifi.seal.soprafs19.utilities;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceNotFoundException;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ch.uzh.ifi.seal.soprafs19.constant.UserStatus.ONLINE;


@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public Boolean isAuthenticated(String token) {
        return userRepository.existsByToken(token) && token != null;
    }

    public Boolean tokenOwnsUser(String token, long userToUpdateId) {
        User dbUser = userRepository.findById(userToUpdateId);
        return isAuthenticated(token) && token.equals(dbUser.getToken());
    }

    public void authenticateUser(String token) throws FailedAuthenticationException {
        if (!userRepository.existsByToken(token)) {
            throw new FailedAuthenticationException();
        }
    }

    public void userTokenInGameById(String token, long gameId) throws ResourceNotFoundException, ResourceActionNotAllowedException, FailedAuthenticationException {
        authenticateUser(token);

        if (!gameRepository.existsById(gameId)) {
            throw new ResourceNotFoundException();
        }

        Game game = gameRepository.findById(gameId);
        User user = userRepository.findByToken(token);

        if (!(game.getUser1().equals(user)) && !(game.getUser2().equals(user))) {
            throw new ResourceActionNotAllowedException();
        }
    }

    public void userTokenIsCurrentTurn(String token, long gameId) throws ResourceActionNotAllowedException {
        Game game = gameRepository.findById(gameId);
        User user = userRepository.findByToken(token);

        if (!game.getCurrentTurn().equals(user)) {
            throw new ResourceActionNotAllowedException();
        }
    }

    public void surrender(String token, long gameId) {

        Game game= gameRepository.findById(gameId);
        User loser = userRepository.findByToken(token);
        User winner = new User();

        if (game.getUser1().getId() == loser.getId()) {
            winner = game.getUser2();
        } else {
            winner = game.getUser1();
        }
        game.setWinnerId(winner.getId());
        game.setLoserId(loser.getId());

        game.setStatus(GameStatus.CANCELED);
        gameRepository.save(game);

        winner.setStatus(ONLINE);
        loser.setStatus(ONLINE);

        userRepository.save(winner);
        userRepository.save(loser);
    }
}
