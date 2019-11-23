package com.loyayz.gaia.exception.autoconfigure;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@RequiredArgsConstructor
public class ExceptionWebfluxAutoConfiguration {

    @ConditionalOnMissingBean(ExceptionWebfluxResolver.class)
    @Component
    @Order(0)
    public class DefaultExceptionWebfluxResolver extends ExceptionWebfluxResolver {

        public DefaultExceptionWebfluxResolver(ExceptionLoggerStrategy loggerStrategy) {
            super(loggerStrategy);
        }

    }

}
