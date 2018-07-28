package xyz.migoo.exception;

import junit.framework.AssertionFailedError;

/**
 * @author xiaomi
 * @date 2017/7/26 17:23
 */
public class ValidatorException extends AssertionFailedError {
    public ValidatorException(String message) {
        super(message);
    }
}
