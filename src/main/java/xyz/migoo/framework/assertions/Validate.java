package xyz.migoo.framework.assertions;

/**
 * @author xiaomi
 * @date 2019-08-10 20:36
 */
public class Validate {

    private String checkPoint;

    private Object except;

    private String func;

    public String getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint) {
        this.checkPoint = checkPoint;
    }

    public Object getExcept() {
        return except;
    }

    public void setExcept(Object except) {
        this.except = except;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"checkPoint\": \"")
                .append(checkPoint).append("\"");
        sb.append(",\"except\": \"")
                .append(except).append("\"");
        sb.append(",\"func\": \"")
                .append(func).append("\"");
        sb.append("}");
        return sb.toString();
    }
}
