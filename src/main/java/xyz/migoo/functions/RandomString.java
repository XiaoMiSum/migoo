package xyz.migoo.functions;

import org.apache.commons.lang3.RandomStringUtils;
import xyz.migoo.exception.MiGooException;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

/**
 * @author xiaomi
 * @date 2019/11/18 17:22
 */
public class RandomString extends AbstractFunction {

    @Override
    public String execute(CompoundVariable parameters) {
        Integer length = parameters.getAsInteger("length");
        if (length == null){
            throw new MiGooException("length con not be null");
        }
        String charsToUse = parameters.getAsString("string").trim();
        if (charsToUse.isEmpty()){
            return RandomStringUtils.random(length);
        }
        return RandomStringUtils.random(length, charsToUse);
    }
}
