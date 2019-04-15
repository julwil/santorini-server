package ch.uzh.ifi.seal.soprafs19.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User does not exist")
public class NotFoundException extends Exception{

    public NotFoundException() {
        super("User does not exist");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
