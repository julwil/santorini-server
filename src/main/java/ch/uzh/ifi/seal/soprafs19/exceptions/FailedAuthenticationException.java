package ch.uzh.ifi.seal.soprafs19.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Failed Authentication. Check your username and password")
public class FailedAuthenticationException extends Exception{

    public FailedAuthenticationException() {
        super("Failed Authentication. Check your username and password");
    }
}
