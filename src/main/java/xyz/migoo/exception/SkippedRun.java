package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2019-02-20 20:33
 */
public class SkippedRun extends MiGooException {

    public SkippedRun(String message) {
        super(message);
    }

    public SkippedRun(String message, Throwable cause) {
        super(message, cause);
    }

}
