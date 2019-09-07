package com.bud.code.annotaction;

import com.bud.code.config.swagger.Swagger2Config;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(Swagger2Config.class)
@Documented
public @interface EnableSwaggerConfig {
}
