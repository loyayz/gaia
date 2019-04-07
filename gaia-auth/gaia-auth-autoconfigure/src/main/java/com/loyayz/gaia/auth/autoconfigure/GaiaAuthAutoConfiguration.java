package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.core.AuthCredentialsConfiguration;
import com.loyayz.gaia.auth.core.AuthResourceConfiguration;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import com.loyayz.gaia.auth.core.resource.DefaultAuthResourceService;
import com.loyayz.gaia.auth.core.user.AuthUserCache;
import com.loyayz.gaia.auth.core.user.AuthUserExtractor;
import com.loyayz.gaia.auth.core.user.DefaultAuthUserExtractor;
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
    @ConditionalOnMissingBean(AuthCredentialsConfiguration.class)
    @ConfigurationProperties(prefix = "gaia.auth.credentials")
    public AuthCredentialsConfiguration authCredentialsConfiguration() {
        return new AuthCredentialsConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean(AuthResourceConfiguration.class)
    @ConfigurationProperties(prefix = "gaia.auth.resource")
    public AuthResourceConfiguration authResourceConfiguration() {
        return new AuthResourceConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean(AuthResourceService.class)
    public AuthResourceService authResourceService(AuthResourceConfiguration authResourceConfiguration) {
        return new DefaultAuthResourceService(authResourceConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean(AuthUserCache.class)
    public AuthUserCache authUserCache(AuthCredentialsConfiguration authCredentialsConfiguration) {
        return new JwtAuthUserCache(authCredentialsConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean(AuthUserExtractor.class)
    public AuthUserExtractor authUserExtractor(AuthUserCache authUserCache) {
        return new DefaultAuthUserExtractor(authUserCache);
    }

}
