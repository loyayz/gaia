package com.loyayz.gaia.exception.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyayz.gaia.exception.ExceptionDisposer;
import com.loyayz.gaia.exception.ExceptionDisposers;
import com.loyayz.gaia.exception.ExceptionResult;
import com.loyayz.gaia.exception.helper.ExceptionWebfluxResolver;
import com.loyayz.gaia.exception.helper.ExceptionWebmvcResolver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
@Slf4j
public class ExceptionAutoConfiguration implements InitializingBean {
    private final List<ExceptionDisposer> disposers;

    public ExceptionAutoConfiguration(List<ExceptionDisposer> disposers) {
        this.disposers = disposers;
    }

    @RestControllerAdvice
    @Order(0)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public class DefaultExceptionWebmvcResolver extends ExceptionWebmvcResolver {
        @ExceptionHandler(value = Throwable.class)
        public ExceptionResult handler(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
            return super.resolve(request, response, exception);
        }
    }

    @Component
    @Order(0)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public class DefaultExceptionWebfluxResolver extends ExceptionWebfluxResolver implements ErrorWebExceptionHandler {
        private ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public Mono<Void> handle(ServerWebExchange exchange, Throwable exception) {
            return super.resolve(exchange, exception)
                    .flatMap(result -> this.writeResponse(exchange, result));
        }

        private Mono<Void> writeResponse(ServerWebExchange exchange, ExceptionResult result) {
            return Mono.defer(() -> {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.valueOf(result.getStatus()));
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                byte[] responseBody = this.responseBody(result);
                DataBuffer buffer = response.bufferFactory().wrap(responseBody);
                return response.writeWith(Mono.just(buffer))
                        .doOnError(error -> DataBufferUtils.release(buffer));
            });
        }

        @SneakyThrows
        private byte[] responseBody(ExceptionResult result) {
            return objectMapper.writeValueAsBytes(result);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExceptionDisposers.addExceptionDisposers(disposers);
    }

}
