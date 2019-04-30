package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2017/5/17 17:23.
 */
public class VariableException extends RuntimeException {
    public VariableException(String message) {
        super(message);
    }

    public VariableException(String message, Throwable cause){
        super(message, cause);
    }
}
