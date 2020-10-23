package io.seanapse.app.products.infrastructure.annotations;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Configuration
public @interface WebSocket {
}
