package xyz.migoo.utils;

import org.slf4j.Logger;

/**
* @author xiaomi
 */
public class Log {

    public static final String LOG_TYPE_TRACE = "logTypeTrace";
    public static final String LOG_TYPE_DEBUG = "logTypeDebug";
    public static final String LOG_TYPE_INFO = "logTypeInfo";
    public static final String LOG_TYPE_WARN = "logTypeWarn";
    public static final String LOG_TYPE_ERROR = "logTypeError";

    private Logger logger;

    public Log(Logger logger) {
        this.logger = logger;
    }

    public void trace(String s) {
        logger.trace(s);
    }

    public void debug(String s) {
        logger.debug(s);
    }

    public void info(String s) {
        logger.info(s);
    }

    public void warn(String s) {
        logger.warn(s);
    }

    public void error(String s) {
        logger.error(s);
    }

    public void error(String s,Throwable t){
        logger.error(s,t);
    }

    public void log(String type, String s) {
        switch (type) {
            case Log.LOG_TYPE_TRACE:
                trace(s);
                break;
            case Log.LOG_TYPE_DEBUG:
                debug(s);
                break;
            case Log.LOG_TYPE_INFO:
                info(s);
                break;
            case Log.LOG_TYPE_WARN:
                warn(s);
                break;
            case Log.LOG_TYPE_ERROR:
                error(s);
                break;
        }
    }
}
