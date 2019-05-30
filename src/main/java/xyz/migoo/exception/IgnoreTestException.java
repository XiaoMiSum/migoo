package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2019-02-20 20:33
 */
public class IgnoreTestException extends Exception {

    public IgnoreTestException(String message) {
        super(message);
    }

    public IgnoreTestException(String message, Throwable cause) {
        super(message, cause);
    }

}
