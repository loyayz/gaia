package com.loyayz.gaia.auth.autoconfigure;

import com.loyayz.gaia.auth.core.AuthCredentialsProperties;
import com.loyayz.gaia.auth.core.AuthResourceProperties;
import com.loyayz.gaia.auth.core.resource.AuthResource;
import com.loyayz.gaia.auth.core.resource.AuthResourcePermission;
import com.loyayz.gaia.auth.core.resource.AuthResourceService;
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

import java.util.List;
import java.util.Map;

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
    @ConditionalOnMissingBean(AuthResourceProperties.class)
    @ConfigurationProperties(prefix = "gaia.auth.resource")
    public AuthResourceProperties authResourceConfiguration() {
        return new AuthResourceProperties();
    }

    @Bean
    @ConditionalOnMissingBean(AuthResourceService.class)
    public AuthResourceService authResourceService(AuthResourceProperties resourceProperties) {
        return new AuthResourceService() {
            @Override
            public List<AuthResource> listPermitResources() {
                return resourceProperties.listPermitResources();
            }

            @Override
            public Map<AuthResource, AuthResourcePermission> listProtectResourcePermission() {
                return resourceProperties.listProtectResources();
            }
        };
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
