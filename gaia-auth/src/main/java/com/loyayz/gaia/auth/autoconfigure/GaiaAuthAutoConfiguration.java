package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import com.loyayz.gaia.auth.core.AuthResourceProperties;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import com.loyayz.gaia.auth.core.resource.DefaultAuthResourceService;
import com.loyayz.gaia.auth.core.user.AuthUserCache;
import com.loyayz.gaia.auth.core.user.strategy.JwtAuthUserCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
public class GaiaAuthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthCredentialsProperties.class)
    @ConfigurationProperties(prefix = "gaia.auth.credentials")
    public AuthCredentialsProperties authCredentialsProperties() {
        return new AuthCredentialsProperties();
    }

    @Bean
    @ConditionalOnMissingBean(AuthResourceProperties.class)
    @ConfigurationProperties(prefix = "gaia.auth.resource")
    public AuthResourceProperties authResourceProperties() {
        return new AuthResourceProperties();
    }

    @Bean
    @ConditionalOnMissingBean(AuthResourceService.class)
    public AuthResourceService authResourceService(AuthResourceProperties resourceProperties) {
        return new DefaultAuthResourceService(resourceProperties);
    }

    @Bean
    @ConditionalOnMissingBean(AuthUserCache.class)
    public AuthUserCache authUserCache(AuthCredentialsProperties credentialsProperties) {
        return new JwtAuthUserCache(credentialsProperties);
    }

}
