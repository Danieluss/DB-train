package db.train.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CannotConnectException extends RuntimeException {
    public CannotConnectException() {
        super();
    }
    public CannotConnectException(String message, Throwable cause) {
        super(message, cause);
    }
    public CannotConnectException(String message) {
        super(message);
    }
    public CannotConnectException(Throwable cause) {
        super(cause);
    }
}
