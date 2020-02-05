package com.loyayz.gaia.data.mybatis.extension;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class BaseMethod extends AbstractMethod {

    protected String sqlWhereEntity(TableInfo table) {
        if (table.isLogicDelete()) {
            String sqlScript = table.getAllSqlWhere(true, true, ENTITY_DOT);
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", ENTITY), true);
            sqlScript += (NEWLINE + table.getLogicDeleteSql(true, true) + NEWLINE);

            sqlScript = SqlScriptUtils.convertChoose(String.format("%s != null", ENTITY), sqlScript,
                    table.getLogicDeleteSql(false, true));
            sqlScript = SqlScriptUtils.convertWhere(sqlScript);
            return NEWLINE + sqlScript;
        } else {
            String sqlScript = table.getAllSqlWhere(false, true, ENTITY_DOT);
            sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", ENTITY), true);
            return NEWLINE + sqlScript;
        }
    }

    protected String sqlOrderBy() {
        String sqlScript = String.format("${%s.toString()} ", MybatisConstants.CONDITION_SORTER_ITEM);
        sqlScript = SqlScriptUtils.convertForeach(sqlScript, MybatisConstants.CONDITION_SORTER, null, MybatisConstants.CONDITION_SORTER_ITEM, COMMA);
        sqlScript = " ORDER BY " + NEWLINE + sqlScript;
        sqlScript = SqlScriptUtils.convertIf(sqlScript,
                String.format("%s != null and %s.length > 0", MybatisConstants.CONDITION_SORTER, MybatisConstants.CONDITION_SORTER), true);
        return NEWLINE + sqlScript;
    }

}
