package com.loyayz.gaia.exception.autoconfigure;

import com.loyayz.gaia.exception.core.ExceptionDefiner;
import com.loyayz.gaia.exception.core.ExceptionResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
