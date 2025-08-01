package testng.xyz.migoo.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MiGoo {

    String value() default "";

    boolean parallel() default false;

    Class<?> type() default Map.class;

    String slice() default "";
}
