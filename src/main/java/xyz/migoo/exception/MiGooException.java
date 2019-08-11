package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2017/7/26 17:23
 */
public class MiGooException extends Error {

    public MiGooException(String message) {
        super(message);
    }

    public MiGooException(String message, Throwable cause) {
        super(message, cause);
    }

}
