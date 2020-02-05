package com.loyayz.gaia.data.autoconfigure;

import com.mongodb.lang.NonNull;
import org.bson.types.Decimal128;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@ConditionalOnClass({MongoCustomConversions.class, Decimal128.class})
public class GaiaMongodbAutoConfiguration {

    /**
     * BigDecimal 转为 mongodb 对应类型，未设置时会默认转为字符串
     */
    @Bean
    @ConditionalOnMissingBean(MongoCustomConversions.class)
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new BigDecimalWritingConverter(),
                new BigDecimalReadingConverter()
        ));
    }

    @WritingConverter
    private static class BigDecimalWritingConverter implements Converter<BigDecimal, Decimal128> {
        @Override
        public Decimal128 convert(@NonNull BigDecimal source) {
            return new Decimal128(source);
        }
    }

    @ReadingConverter
    private static class BigDecimalReadingConverter implements Converter<Decimal128, BigDecimal> {
        @Override
        public BigDecimal convert(@NonNull Decimal128 source) {
            return source.bigDecimalValue();
        }
    }

}
