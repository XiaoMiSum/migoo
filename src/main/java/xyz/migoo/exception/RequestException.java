package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2017/5/17 17:23.
 */
public class RequestException extends RuntimeException {
    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause){
        super(message, cause);
    }
}
