package com.msht.examination.security.bind.annotation;

import java.lang.annotation.*;

import com.msht.examination.security.Constants;


/**
 * 绑定当前登录的用户
 * @author lindaofen
 *
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * 当前用户在request中的名字
     *
     * @return
     */
    String value() default Constants.CURRENT_USER;

}
