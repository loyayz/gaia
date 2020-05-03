package com.loyayz.gaia.data.mybatis;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.loyayz.gaia.data.PageModel;
import com.loyayz.gaia.data.Sorter;
import com.loyayz.gaia.data.mybatis.extension.MybatisConstants;
import com.loyayz.gaia.data.mybatis.extension.Pages;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class BaseEntity<T extends BaseEntity> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 插入（字段选择插入）
     */
    public boolean insert() {
        SqlSession sqlSession = sqlSession();
        try {
            return SqlHelper.retBool(sqlSession.insert(sqlStatement(MybatisConstants.METHOD_INSERT), this));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 根据主键删除
     */
    public boolean deleteById() {
        return deleteById(pkVal());
    }

    public boolean deleteById(Serializable id) {
        Assert.isFalse(StringUtils.checkValNull(id), "deleteById primaryKey is null.");
        SqlSession sqlSession = sqlSession();
        try {
            return SqlHelper.retBool(sqlSession.delete(sqlStatement(MybatisConstants.METHOD_DELETE_BY_ID), id));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 根据主键删除
     */
    public boolean deleteByIds(Collection<Serializable> ids) {
        Assert.isFalse(CollectionUtils.isEmpty(ids), "deleteByIds primaryKeys is empty.");
        Map<String, Object> map = new HashMap<>(1);
        map.put(Constants.COLLECTION, ids);
        SqlSession sqlSession = sqlSession();
        try {
            return SqlHelper.retBool(sqlSession.delete(sqlStatement(MybatisConstants.METHOD_DELETE_BY_IDS), map));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 根据主键更新（字段选择更新）
     */
    public boolean updateById() {
        return updateById(pkVal());
    }

    public boolean updateById(Serializable id) {
        Assert.isFalse(StringUtils.checkValNull(id), "updateById primaryKey is null.");
        Map<String, Object> map = new HashMap<>(1);
        map.put(Constants.ENTITY, this);
        SqlSession sqlSession = sqlSession();
        try {
            return SqlHelper.retBool(sqlSession.update(sqlStatement(MybatisConstants.METHOD_UPDATE_BY_ID), map));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 根据主键查询
     */
    public T getById() {
        return getById(pkVal());
    }

    public T getById(Serializable id) {
        Assert.isFalse(StringUtils.checkValNull(id), "getById primaryKey is null.");
        SqlSession sqlSession = sqlSession();
        try {
            return sqlSession.selectOne(sqlStatement(MybatisConstants.METHOD_GET_BY_ID), id);
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 列表：根据 ids 查询
     */
    public List<T> listByIds(Collection<Serializable> ids, Sorter... sorters) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Constants.COLLECTION, ids);
        map.put(MybatisConstants.CONDITION_SORTER, sorters);
        SqlSession sqlSession = sqlSession();
        try {
            return sqlSession.selectList(sqlStatement(MybatisConstants.METHOD_LIST_BY_IDS), map);
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 列表：根据字段值查询
     */
    public List<T> listByCondition(Sorter... sorters) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Constants.ENTITY, this);
        map.put(MybatisConstants.CONDITION_SORTER, sorters);
        SqlSession sqlSession = sqlSession();
        try {
            return sqlSession.selectList(sqlStatement(MybatisConstants.METHOD_LIST_BY_CONDITION), map);
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 分页：根据字段值查询
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    public PageModel<T> pageByCondition(int pageNum, int pageSize, Sorter... sorters) {
        return Pages.doSelectPage(pageNum, pageSize,
                () -> this.listByCondition(sorters),
                this::countByCondition);
    }

    /**
     * 查询总数
     */
    public Integer countByCondition() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Constants.ENTITY, this);
        SqlSession sqlSession = sqlSession();
        try {
            return SqlHelper.retCount(sqlSession.<Integer>selectOne(sqlStatement(MybatisConstants.METHOD_COUNT_BY_CONDITION), map));
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 是否存在
     */
    public Boolean existByCondition() {
        Integer num = this.countByCondition();
        return num != null && num > 0;
    }

    /**
     * 获取Session 默认自动提交
     */
    protected SqlSession sqlSession() {
        return SqlHelper.sqlSession(getClass());
    }

    /**
     * 获取SqlStatement
     *
     * @param sqlMethod sqlMethod
     */
    protected String sqlStatement(String sqlMethod) {
        return SqlHelper.table(getClass()).getSqlStatement(sqlMethod);
    }

    /**
     * 主键值
     */
    protected Serializable pkVal() {
        return (Serializable) ReflectionKit.getMethodValue(this, TableInfoHelper.getTableInfo(getClass()).getKeyProperty());
    }

    /**
     * 释放sqlSession
     *
     * @param sqlSession session
     */
    protected void closeSqlSession(SqlSession sqlSession) {
        SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(getClass()));
    }

    protected GlobalConfig getGlobalConfig() {
        return GlobalConfigUtils.getGlobalConfig(TableInfoHelper.getTableInfo(getClass()).getConfiguration());
    }

    protected IdentifierGenerator identifierGenerator() {
        return this.getGlobalConfig().getIdentifierGenerator();
    }

}
