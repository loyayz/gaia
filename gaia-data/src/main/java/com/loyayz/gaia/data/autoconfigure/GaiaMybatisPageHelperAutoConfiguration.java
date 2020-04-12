package com.loyayz.gaia.data.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter({GaiaMybatisPlusAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
@EnableConfigurationProperties({GaiaMybatisProperties.class})
public class GaiaMybatisPageHelperAutoConfiguration implements InitializingBean {
    private List<SqlSessionFactory> sqlSessionFactoryList;
    private GaiaMybatisProperties mybatisProperties;

    public GaiaMybatisPageHelperAutoConfiguration(List<SqlSessionFactory> sqlSessionFactoryList,
                                                  GaiaMybatisProperties mybatisProperties) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
        this.mybatisProperties = mybatisProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.addPageHelper();
    }

    private void addPageHelper() {
        Properties properties = this.mybatisProperties.getPageHelperProperties();
        PageInterceptor interceptor = new PageInterceptor();
        interceptor.setProperties(properties);
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }
    }

}
