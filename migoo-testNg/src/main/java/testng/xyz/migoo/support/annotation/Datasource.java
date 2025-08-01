package testng.xyz.migoo.support.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * 数据驱动增强注解，以支持从文件中读取 migoo测试用例
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Datasource {

    /**
     * 数据源：文件路径
     *
     * @return 数据源
     */
    String value() default "";

    /**
     * 是否并行执行
     *
     * @return 是否并行执行
     */
    boolean parallel() default false;

    /**
     * 返回参数类型
     *
     * @return 返回参数类型
     */
    Class<?> type() default Map.class;
}
