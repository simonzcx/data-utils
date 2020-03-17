package com.mingcloud.data.common;

import java.lang.annotation.*;

/**
 * 注解生命周期作用范围
 */
@Retention(RetentionPolicy.RUNTIME)
/**
 *注解作用在参数上
 */
@Target({ElementType.METHOD,ElementType.PARAMETER})

@Documented
@Inherited
public @interface DBChange {
}
