package xyz.migoo.functions;

import org.apache.commons.lang3.RandomStringUtils;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

/**
 * @author xiaomi
 * @date 2019/11/18 17:22
 */
public class RandomString extends AbstractFunction {

    @Override
    public String execute(CompoundVariable parameters) throws ExecuteError {
        if (parameters.isEmpty()){
            throw new ExecuteError("parameters con not be null");
        }
        Integer length = parameters.getInteger("length");
        if (length == null){
            throw new ExecuteError("length con not be null");
        }
        String charsToUse = parameters.getString("string").trim();
        if (charsToUse.isEmpty()){
            return RandomStringUtils.randomAlphabetic(length);
        }
        return RandomStringUtils.random(length, charsToUse);
    }
}
