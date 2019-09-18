package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2019-02-20 20:33
 */
public class ExtenderException extends Exception {

    public ExtenderException(String message) {
        super(message);
    }

    public ExtenderException(String message, Throwable cause) {
        super(message, cause);
    }

}
