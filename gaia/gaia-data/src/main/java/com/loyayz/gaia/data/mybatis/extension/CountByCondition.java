package com.loyayz.gaia.data.mybatis.extension;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class CountByCondition extends BaseMethod {
    private static final String METHOD_NAME = MybatisConstants.METHOD_COUNT_BY_CONDITION;

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>SELECT COUNT(*) FROM %s %s </script>";
        sql = String.format(sql, tableInfo.getTableName(), super.sqlWhereEntity(tableInfo));
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, METHOD_NAME, sqlSource, Integer.class);
    }

    @Override
    public String getMethod(SqlMethod sqlMethod) {
        return METHOD_NAME;
    }

}
