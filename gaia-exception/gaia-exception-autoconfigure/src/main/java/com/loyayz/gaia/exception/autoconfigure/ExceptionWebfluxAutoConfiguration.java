package com.loyayz.gaia.exception.autoconfigure;

import com.loyayz.gaia.commons.exception.ExceptionResolver;
import com.loyayz.gaia.commons.exception.WebfluxExceptionResolver;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@AutoConfigureBefore(ErrorWebFluxAutoConfiguration.class)
public class ExceptionWebfluxAutoConfiguration {

    @ConditionalOnMissingBean(value = {ErrorWebExceptionHandler.class, WebExceptionHandler.class})
    @Component
    @Order(0)
    public class DefaultWebfluxExceptionResolver extends WebfluxExceptionResolver
            implements ErrorWebExceptionHandler, WebExceptionHandler {

        DefaultWebfluxExceptionResolver(ExceptionResolver resolver) {
            super(resolver);
        }

        @Override
        public Mono<Void> handle(ServerWebExchange exchange, Throwable exception) {
            return super.handlerException(exchange, exception);
        }

    }

}
