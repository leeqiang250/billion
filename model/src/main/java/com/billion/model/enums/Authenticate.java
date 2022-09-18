package com.billion.model.enums;

import java.lang.annotation.*;

/**
 * @author liqiang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Authenticate {

    AuthenticateType value() default AuthenticateType.PROTECT;

}