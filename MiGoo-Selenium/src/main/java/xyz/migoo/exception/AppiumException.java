package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2017/5/17 17:23.
 */
public class AppiumException extends RuntimeException {
    public AppiumException(String message) {
        super(message);
    }

    public AppiumException(String message, Throwable t){
        super(message, t);
    }
}
