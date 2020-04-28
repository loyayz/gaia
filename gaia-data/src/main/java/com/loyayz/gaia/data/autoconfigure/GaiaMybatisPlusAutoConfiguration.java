package com.loyayz.gaia.data.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.loyayz.gaia.data.mybatis.AbstractEntity;
import com.loyayz.gaia.data.mybatis.extension.DefaultSqlInjector;
import com.loyayz.gaia.util.Sequence;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@AutoConfigureAfter({GaiaDataPropertiesAutoConfiguration.class})
@AutoConfigureBefore({MybatisPlusAutoConfiguration.class})
public class GaiaMybatisPlusAutoConfiguration {
    private GaiaMybatisProperties mybatisProperties;

    public GaiaMybatisPlusAutoConfiguration(GaiaMybatisProperties mybatisProperties) {
        this.mybatisProperties = mybatisProperties;
    }

    @Bean
    @ConditionalOnClass(ISqlInjector.class)
    @ConditionalOnMissingBean(ISqlInjector.class)
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector();
    }

    @Bean
    @ConditionalOnClass(IdentifierGenerator.class)
    @ConditionalOnMissingBean(IdentifierGenerator.class)
    public IdentifierGenerator identifierGenerator() {
        return new IdentifierGenerator() {
            private final Sequence sequence = new Sequence(mybatisProperties.getIdEpoch());

            @Override
            public Number nextId(Object entity) {
                return sequence.nextId();
            }

        };
    }

    @Bean
    @ConditionalOnClass(MetaObjectHandler.class)
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                Object originalObject = metaObject.getOriginalObject();
                if (originalObject instanceof AbstractEntity) {
                    AbstractEntity entity = (AbstractEntity) originalObject;
                    Date current = new Date();
                    entity.setGmtCreate(current);
                    entity.setGmtModified(current);
                }
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                Object originalObject = metaObject.getOriginalObject();
                if (originalObject instanceof AbstractEntity) {
                    ((AbstractEntity) originalObject).setGmtModified(new Date());
                }
            }
        };
    }

}
