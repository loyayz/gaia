package com.loyayz.gaia.exception.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ExceptionWebmvcAutoConfiguration {

    @ConditionalOnMissingBean(ExceptionWebmvcResolver.class)
    @RestControllerAdvice
    @Order(0)
    public class DefaultExceptionWebmvcResolver extends ExceptionWebmvcResolver {

        public DefaultExceptionWebmvcResolver(ExceptionLoggerStrategy loggerStrategy) {
            super(loggerStrategy);
        }

    }

}
