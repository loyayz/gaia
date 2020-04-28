package com.loyayz.gaia.data.autoconfigure;

import org.bson.types.Decimal128;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@EnableConfigurationProperties({GaiaMybatisProperties.class})
public class GaiaDataPropertiesAutoConfiguration {

    @Configuration
    @ConditionalOnClass({MongoCustomConversions.class, Decimal128.class})
    public static class GaiaMongoAutoConfiguration {
        /**
         * mongodb
         * BigDecimal 转为 mongodb 对应类型，未设置时会默认转为字符串
         */
        @Bean
        @ConditionalOnMissingBean(MongoCustomConversions.class)
        public MongoCustomConversions mongoCustomConversions() {
            return new MongoCustomConversions(Arrays.asList(
                    (Converter<BigDecimal, Decimal128>) Decimal128::new,
                    (Converter<Decimal128, BigDecimal>) Decimal128::bigDecimalValue
            ));
        }
    }

}
