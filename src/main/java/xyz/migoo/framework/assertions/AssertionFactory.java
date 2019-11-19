package xyz.migoo.framework.assertions;

import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.config.CaseKeys;

import static xyz.migoo.framework.config.Platform.*;

/**
 * @author xiaomi
 * @date 2019-04-13 22:12
 */
public class AssertionFactory {

    private AssertionFactory(){}

    public static AbstractAssertion getAssertion(String check){
        switch (assertionType(check)){
            case CaseKeys.JSON:
                return new JSONAssertion(check);
            case CaseKeys.STATUS:
                return new ResponseCodeAssertion();
            case CaseKeys.BODY:
                return new ResponseAssertion();
            default:
                try {
                    return (AbstractAssertion)Class.forName(check).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ExecuteError("assert class not found: " + check);
                }
        }
    }

    private static String assertionType(String searchChar){
        if (CHECK_BODY.contains(searchChar)) {
            return CaseKeys.BODY;
        }
        if (CHECK_CODE.contains(searchChar)) {
            return CaseKeys.STATUS;
        }
        if (isJson(searchChar)) {
            return CaseKeys.JSON;
        }
        return searchChar;
    }

}
