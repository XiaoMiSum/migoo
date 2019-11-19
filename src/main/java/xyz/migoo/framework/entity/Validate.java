package xyz.migoo.framework.entity;

/**
 * @author xiaomi
 * @date 2019-08-10 20:36
 */
public class Validate {

    private String check;

    private Object expect;

    private Object actual;

    private String func;

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public Object getExpect() {
        return expect;
    }

    public void setExpect(Object expect) {
        this.expect = expect;
    }

    public Object getActual() {
        return actual;
    }

    public void setActual(Object actual) {
        this.actual = actual;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    @Override
    public String toString() {
        return new StringBuilder("{")
                .append("\"check\": \"").append(check).append("\"")
                .append(",")
                .append("\"expect\": \"").append(expect).append("\"")
                .append(",")
                .append("\"actual\": \"").append(actual).append("\"")
                .append(",")
                .append("\"func\": \"")
                .append(func).append("\"")
                .append("}")
                .toString();
    }

}
