/**
 * 
 */
package com.msht.examination.common.log.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ METHOD, PARAMETER })
/**
 * @author lindaofen
 *
 */
public @interface OperLog {

    String module()  default "";  
    String methods()  default ""; 
}
