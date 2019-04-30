package xyz.migoo.exception;

/**
 * @author xiaomi
 * @date 2017/5/17 16:51.
 */
public class BaseException extends Exception {

    public static final Integer SUCCESS = 999;
    public static final Integer FAIL = 100;
    private Integer code;
    private String message;
    private String url;
    private String data;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message, Integer code, String message1, String url, String data) {
        super(message);
        this.code = code;
        this.message = message1;
        this.url = url;
        this.data = data;
    }

    public BaseException(String message, Throwable cause, Integer code, String message1, String url, String data) {
        super(message, cause);
        this.code = code;
        this.message = message1;
        this.url = url;
        this.data = data;
    }

    public BaseException(Throwable cause, Integer code, String message, String url, String data) {
        super(cause);
        this.code = code;
        this.message = message;
        this.url = url;
        this.data = data;
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code, String message1, String url, String data) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.message = message1;
        this.url = url;
        this.data = data;
    }
}
