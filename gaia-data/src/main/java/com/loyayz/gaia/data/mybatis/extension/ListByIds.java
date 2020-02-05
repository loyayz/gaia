package com.loyayz.gaia.data.mybatis.extension;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author loyayz (loyayz@foxmail.com)
 * @see com.baomidou.mybatisplus.core.injector.methods.SelectBatchByIds
 */
public class ListByIds extends BaseMethod {
    private static final String METHOD_NAME = MybatisConstants.METHOD_LIST_BY_IDS;

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>SELECT %s FROM %s WHERE %s IN (%s) %s %s</script>";
        sql = String.format(sql,
                sqlSelectColumns(tableInfo, false), tableInfo.getTableName(), tableInfo.getKeyColumn(),
                SqlScriptUtils.convertForeach("#{item}", COLLECTION, null, "item", COMMA),
                tableInfo.getLogicDeleteSql(true, true),
                super.sqlOrderBy());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
        return super.addSelectMappedStatementForTable(mapperClass, METHOD_NAME, sqlSource, tableInfo);
    }

    @Override
    public String getMethod(SqlMethod sqlMethod) {
        return METHOD_NAME;
    }

}
