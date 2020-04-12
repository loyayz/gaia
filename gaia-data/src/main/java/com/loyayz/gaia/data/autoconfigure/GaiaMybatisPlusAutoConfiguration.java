package com.loyayz.gaia.data.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.loyayz.gaia.data.mybatis.extension.DefaultSqlInjector;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@ConditionalOnClass(ISqlInjector.class)
@AutoConfigureBefore({MybatisPlusAutoConfiguration.class})
public class GaiaMybatisPlusAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ISqlInjector.class)
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector();
    }

}
