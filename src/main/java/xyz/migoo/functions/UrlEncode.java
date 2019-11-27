package xyz.migoo.functions;

import xyz.migoo.exception.ExtenderException;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author xiaomi
 * @date 2019/11/18 17:22
 */
public class UrlEncode extends AbstractFunction {

    @Override
    public String execute(CompoundVariable parameters) throws ExtenderException {
        try {
            return URLEncoder.encode(parameters.getAsString("string"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ExtenderException("url encode exception", e);
        }
    }
}
