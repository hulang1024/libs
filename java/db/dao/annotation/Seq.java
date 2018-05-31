package db.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用序列生成值
 * @author hulang
 * @sice 2018年5月6日
 * @vesion 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Seq {
    /**
     * 序列名称
     * @return
     */
    String name() default ""; 
}
