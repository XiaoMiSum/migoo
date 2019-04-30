package xyz.migoo.assertions;

import xyz.migoo.http.Response;
import xyz.migoo.utils.HtmlUtil;

/**
 * @author xiaomi
 * @date 2019-04-13 21:37
 */
public class HTMLAssertion extends AbstractAssertion {

    private String xPath;

    HTMLAssertion(String xPath){
        this.xPath = xPath;
    }

    @Override
    public void setActual(Object actual) {
        Response response = (Response) actual;
        this.actual = HtmlUtil.parse(response.body(), xPath.split("."));
    }
}
