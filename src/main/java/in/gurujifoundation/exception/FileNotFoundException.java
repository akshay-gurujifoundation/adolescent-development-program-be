package in.gurujifoundation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Bad Request Error Exception.
 *
 * @author Pratik
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundException extends RuntimeException {

    /**
     * All args constructor.
     *
     * @param message of the bad request exception.
     */
    public FileNotFoundException(final String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}