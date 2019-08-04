package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2017/7/26 17:23
 */
public class AssertionFailure extends MiGooException {

    public AssertionFailure(String message) {
        super(message);
    }

    public AssertionFailure(String message, Throwable cause) {
        super(message, cause);
    }

}
