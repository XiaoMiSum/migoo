package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2019-08-04 09:22
 */
public class ExecuteError extends MiGooException {

    public ExecuteError(String message) {
        super(message);
    }

    public ExecuteError(String message, Throwable cause) {
        super(message, cause);
    }
}
