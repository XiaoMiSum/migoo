package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2017/5/17 16:51.
 */
public class BaseException extends Exception {

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

}
