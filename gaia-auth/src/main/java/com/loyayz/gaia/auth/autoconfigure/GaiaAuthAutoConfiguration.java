package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
import com.loyayz.gaia.auth.core.resource.impl.PropertiesAuthResourceService;
import com.loyayz.gaia.auth.core.user.AuthUserCache;
import com.loyayz.gaia.auth.core.user.AuthUserCacheItemConverter;
import com.loyayz.gaia.auth.core.user.AuthUserService;
import com.loyayz.gaia.auth.core.user.impl.DefaultAuthUserCacheItemConverter;
import com.loyayz.gaia.auth.core.user.impl.DefaultAuthUserService;
import com.loyayz.gaia.auth.core.user.impl.JwtAuthUserCache;
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
    public AuthCredentialsProperties authCredentialsConfiguration() {
        return new AuthCredentialsProperties();
    }

    @Bean
    @ConditionalOnMissingBean(AuthResourceService.class)
    @ConfigurationProperties(prefix = "gaia.auth.resource")
    public AuthResourceService authResourceService() {
        return new PropertiesAuthResourceService();
    }

    @Bean
    @ConditionalOnMissingBean(AuthUserCache.class)
    public AuthUserCache authUserCache(AuthCredentialsProperties credentialsProperties) {
        return new JwtAuthUserCache(credentialsProperties);
    }

    @Bean
    @ConditionalOnMissingBean(AuthUserCacheItemConverter.class)
    public AuthUserCacheItemConverter authUserCacheItemConverter() {
        return new DefaultAuthUserCacheItemConverter();
    }

    @Bean
    @ConditionalOnMissingBean(AuthUserService.class)
    public AuthUserService authUserService(AuthUserCache authUserCache,
                                           AuthUserCacheItemConverter authUserCacheItemConverter) {
        return new DefaultAuthUserService(authUserCache, authUserCacheItemConverter);
    }

}
