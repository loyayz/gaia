package com.loyayz.gaia.exception.autoconfigure;

import com.loyayz.gaia.exception.core.ExceptionResolver;
import com.loyayz.gaia.exception.core.ExceptionResult;
import com.loyayz.gaia.exception.core.WebExceptionResolver;
import com.loyayz.gaia.exception.core.WebmvcExceptionResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ExceptionWebmvcAutoConfiguration {

    @ConditionalOnMissingBean(value = {WebExceptionResolver.class})
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
