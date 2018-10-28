package io.ostenant.rpc.thrift.client.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ThriftClient {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    String serviceId();

    double version() default 1.0;

    Class<?> refer();

}
