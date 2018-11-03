package io.ostenant.rpc.thrift.client.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ThriftRefer {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}
