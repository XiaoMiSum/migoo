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

    private String result = "skipped";

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"check\": \"").append(check).append("\"").append(",");
        if (expect instanceof String) {
            sb.append("\"expect\": \"").append(expect).append("\"").append(",");
        } else {
            sb.append("\"expect\": ").append(expect).append(",");
        }
        return sb.append("\"actual\": \"").append(actual).append("\"").append(",")
                .append("\"func\": \"").append(func).append("\"").append(",")
                .append("\"result\": \"").append(result).append("\"").append("}")
                .toString();
    }
}
