package com.loyayz.gaia.data.mybatis.extension;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class ListByCondition extends BaseMethod {
    private static final String METHOD_NAME = MybatisConstants.METHOD_LIST_BY_CONDITION;

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>SELECT %s FROM %s %s %s</script>";
        String selectColumns = sqlSelectColumns(tableInfo, false);
        String tableName = tableInfo.getTableName();
        String condition = super.sqlWhereEntity(tableInfo);
        String sort = super.sqlOrderBy();
        sql = String.format(sql, selectColumns, tableName, condition, sort);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return super.addSelectMappedStatementForTable(mapperClass, METHOD_NAME, sqlSource, tableInfo);
    }

    @Override
    public String getMethod(SqlMethod sqlMethod) {
        return METHOD_NAME;
    }

}
