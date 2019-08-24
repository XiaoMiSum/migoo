package xyz.migoo.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Date;

import static xyz.migoo.utils.DateUtil.YYYY_MM_DD_HH_MM_SS;

/**
* @author xiaomi
 */
public class MiGooLog {

    private static final Logger LOGGER = LoggerFactory.getLogger("MiGoo-Logger");

    public static void log(String message) {
        LOGGER.info(message);
    }

    public static void log(String message, Object... msg) {
        LOGGER.info(message, msg);
    }

    public static void log(String message, Throwable t) {
        LOGGER.error(message, t);
    }

    public static void debug(String message) {
        LOGGER.debug(message);
    }

    public static void debug(String message, Object... msg) {
        LOGGER.debug(message, msg);
    }
}
