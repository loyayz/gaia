package com.loyayz.gaia.auth.core;

import lombok.Data;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthCorsProperties {

    Map<String, CorsConfig> getCorsConfigs();

    @Data
    class CorsConfig {
        private List<String> allowedOrigins;
        private List<String> allowedMethods;
        private List<String> allowedHeaders;
        private List<String> exposedHeaders;
        private Boolean allowCredentials;
        private Long maxAge;

        public CorsConfiguration toCorsConfiguration() {
            CorsConfiguration result = new CorsConfiguration();
            result.setAllowedOrigins(this.getAllowedOrigins());
            result.setAllowedMethods(this.getAllowedMethods());
            result.setAllowedHeaders(this.getAllowedHeaders());
            result.setExposedHeaders(this.getExposedHeaders());
            result.setAllowCredentials(this.getAllowCredentials());
            result.setMaxAge(this.getMaxAge());
            return result;
        }
    }

}
