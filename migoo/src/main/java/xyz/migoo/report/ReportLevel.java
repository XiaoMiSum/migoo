package xyz.migoo.report;

public enum ReportLevel {

    /**
     * 项目级
     */
    suite,
    /**
     * 接口级
     */
    set,
    /**
     * 用例级
     */
    testcase,
    /**
     * 取样器
     */
    sampler,
    ;


    public static ReportLevel of(int level) {
        return switch (level) {
            case 1 -> suite;
            case 2 -> set;
            case 3 -> testcase;
            case 4 -> sampler;
            default -> throw new RuntimeException("no matching levels");
        };
    }

    public boolean isSuite() {
        return suite == this;
    }

    public boolean isSet() {
        return set == this;
    }

    public boolean isTestcase() {
        return testcase == this;
    }

    public boolean isSampler() {
        return sampler == this;
    }
}

