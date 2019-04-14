package ch.uzh.ifi.seal.soprafs19.utilities;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional

public class Utilities {


    public void copyAttributes(User toUser, User fromUser) {


        toUser.setUsername(
                fromUser.getUsername() != null ?
                        fromUser.getUsername() : toUser.getUsername()
        );
        toUser.setPassword(
                fromUser.getPassword() != null ?
                        fromUser.getPassword() :  toUser.getPassword()
        );
        toUser.setName(fromUser.getName());
        toUser.setBirthday(fromUser.getBirthday());

    }
}
