package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2017/5/17 17:23.
 */
public class SeleniumException extends RuntimeException {
    public SeleniumException(String message) {
        super(message);
    }

    public SeleniumException(String message, Throwable t){
        super(message, t);
    }
}
