package com.bud.code.annotaction;

import com.bud.code.config.swagger.SwaggerCorsFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SwaggerCorsFilter.class)
@Documented
public @interface EnableCorsFilter {
}
