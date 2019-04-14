package ch.uzh.ifi.seal.soprafs19.utilities;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.exceptions.FailedAuthenticationException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ResourceActionNotAllowedException;
import ch.uzh.ifi.seal.soprafs19.exceptions.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class Authentication  {

    private static UserRepository userRepository;

    @Autowired
    public Authentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updateUser(String originatorToken, long userToUpdateId, User userToUpdate) throws ResourceActionNotAllowedException, FailedAuthenticationException, UsernameAlreadyExistsException {
        /* Check if token is valid */
        if (userRepository.existsByToken(originatorToken)) {

            // Check if token from request-originator is related to the user-id from the user to be updated
            if (userRepository.findByToken(originatorToken).getId().equals(userToUpdateId)) {
                User dbUser = userRepository.findById(userToUpdateId);

                if (dbUser.getUsername().equals(userToUpdate.getUsername()) ||
                        !userRepository.existsByUsername(userToUpdate.getUsername())) {
                    userRepository.save(dbUser);

                    Utilities utilities = new Utilities();
                    utilities.copyAttributes(dbUser, userToUpdate);


                    return dbUser;

                } else {
                    throw new UsernameAlreadyExistsException();
                }

            } else {
                throw new ResourceActionNotAllowedException();
            }
        } else {
            throw new FailedAuthenticationException();
        }


    }

}
