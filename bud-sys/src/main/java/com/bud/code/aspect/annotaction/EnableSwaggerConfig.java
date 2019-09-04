package com.bud.code.aspect.annotaction;

import com.bud.code.config.Swagger2Config;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(Swagger2Config.class)
@Documented
public @interface EnableSwaggerConfig {
}
