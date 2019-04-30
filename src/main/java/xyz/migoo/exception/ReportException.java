package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2017/5/17 17:23.
 */
public class ReportException extends RuntimeException {
    public ReportException(String message) {
        super(message);
    }

    public ReportException(String message, Throwable cause){
        super(message, cause);
    }
}
