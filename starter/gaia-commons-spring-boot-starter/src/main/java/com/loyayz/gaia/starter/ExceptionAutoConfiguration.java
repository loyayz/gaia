package com.loyayz.gaia.starter;

import com.loyayz.gaia.commons.exception.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
public class ExceptionAutoConfiguration {
    private final List<ExceptionDefiner> definers;

    public ExceptionAutoConfiguration(List<ExceptionDefiner> disposers) {
        this.definers = disposers;
    }

    @ConditionalOnMissingBean(value = {ExceptionResolver.class})
    @Bean
    public ExceptionResolver exceptionResolver() {
        return new ExceptionResolver(this.definers);
    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public class ExceptionWebfluxAutoConfiguration {

        @ConditionalOnMissingBean(value = {ErrorWebExceptionHandler.class})
        @Component
        @Order(0)
        public class DefaultWebfluxExceptionResolver extends WebfluxExceptionResolver implements ErrorWebExceptionHandler {

            DefaultWebfluxExceptionResolver(ExceptionResolver resolver) {
                super(resolver);
            }

            @Override
            public Mono<Void> handle(ServerWebExchange exchange, Throwable exception) {
                return super.handlerException(exchange, exception);
            }

        }

    }

    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public class ExceptionWebmvcAutoConfiguration {

        @ConditionalOnMissingBean(value = {WebmvcExceptionResolver.class})
        @RestControllerAdvice
        @Order(0)
        public class DefaultWebmvcExceptionResolver extends WebmvcExceptionResolver {

            DefaultWebmvcExceptionResolver(ExceptionResolver resolver) {
                super(resolver);
            }

            @ExceptionHandler(value = Throwable.class)
            public ExceptionResult handler(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
                return super.handlerException(request, response, exception);
            }

        }

    }

}
