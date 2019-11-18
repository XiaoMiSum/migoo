package xyz.migoo.functions;

import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;
import xyz.migoo.utils.digest.GoogleAuthenticator;

/**
 * @author yacheng.xiao
 * @date 2019/11/18 17:22
 */
public class GoogleAuthCode extends AbstractFunction {

    @Override
    public String execute(CompoundVariable parameters) {
        return GoogleAuthenticator.generateAuthCode(parameters.getAsString("secretKey"));
    }
}
