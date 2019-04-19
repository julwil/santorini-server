package ch.uzh.ifi.seal.soprafs19.utilities;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AuthenticationService {

    private static UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean isAuthenticated(String token) {
        return userRepository.existsByToken(token) && token != null;
    }

    public Boolean tokenOwnsUser(String token, long userToUpdateId) {
        User dbUser = userRepository.findById(userToUpdateId);
        return isAuthenticated(token) && token.equals(dbUser.getToken());
    }
}
