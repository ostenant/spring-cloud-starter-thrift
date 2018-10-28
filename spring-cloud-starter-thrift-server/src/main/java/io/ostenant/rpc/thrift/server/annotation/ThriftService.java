package io.ostenant.rpc.thrift.server.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface ThriftService {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    double version() default 1.0;
}
