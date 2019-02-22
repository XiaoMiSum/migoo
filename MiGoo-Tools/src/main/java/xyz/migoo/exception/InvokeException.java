package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2019-02-20 20:33
 */
public class InvokeException extends Exception {

    public InvokeException(String message) {
        super(message);
    }

    public InvokeException(String message, Throwable cause) {
        super(message, cause);
    }

}
