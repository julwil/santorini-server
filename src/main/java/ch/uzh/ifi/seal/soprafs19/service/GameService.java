package ch.uzh.ifi.seal.soprafs19.service;
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

    private final UserRepository userRepository;


    @Autowired
    public GameService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Game getGameById(long id){
        return null;
    }


    public String createGame(Game game){
        return "3";
    }
}
