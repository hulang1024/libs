package db.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键
 * @author hulang
 * @sice 2017年8月21日
 * @vesion 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PK {
    /**
     * 主键名称
     * @return
     */
    String name() default ""; 
}
