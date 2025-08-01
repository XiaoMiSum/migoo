package testng.xyz.migoo.support.annotation;

public @interface DataFilter {
    // todo  要定义切片方式，实现数据切片
    String slice() default "";
}
