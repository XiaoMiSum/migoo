package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2017/5/17 17:23.
 */
public class ParserException extends RuntimeException {
    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause){
        super(message, cause);
    }
}
