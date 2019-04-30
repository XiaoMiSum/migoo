package xyz.migoo.assertions;

import xyz.migoo.config.CaseKeys;

import static xyz.migoo.config.Platform.*;

/**
 * @author xiaomi
 * @date 2019-04-13 22:12
 */
public class AssertionFactory {

    private AssertionFactory(){}

    public static AbstractAssertion getAssertion(String check){
        switch (new AssertionFactory().assertionType(check)){
            case CaseKeys.EVAL_ACTUAL_BY_JSON:
                return new JSONAssertion(check);
            case CaseKeys.EVAL_ACTUAL_BY_HTML:
                return new HTMLAssertion(check);
            case CaseKeys.EVAL_ACTUAL_BY_STATUS:
                return new ResponseCodeAssertion();
            case CaseKeys.VALIDATE_TYPE_CUSTOM_FUNCTION:
                return new CustomAssertion();
            default:
                return new ResponseAssertion();
        }
    }

    private String assertionType(String searchChar){
        if (CHECK_BODY.contains(searchChar)) {
            return CaseKeys.EVAL_ACTUAL_BY_BODY;
        }
        if (CHECK_CODE.contains(searchChar)) {
            return CaseKeys.EVAL_ACTUAL_BY_STATUS;
        }
        if (isJson(searchChar)) {
            return CaseKeys.EVAL_ACTUAL_BY_JSON;
        }
        if (isHtml(searchChar)){
            return CaseKeys.EVAL_ACTUAL_BY_HTML;
        }
        if (isCustom(searchChar)){
            return CaseKeys.VALIDATE_TYPE_CUSTOM_FUNCTION;
        }
        return "";
    }

}
