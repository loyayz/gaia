package com.loyayz.gaia.data.mybatis.extension;

import com.loyayz.gaia.data.mybatis.BaseEntityMapper;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface MybatisConstants {

    String CONDITION_SORTER = "sc";
    String CONDITION_SORTER_ITEM = "sci";

    /**
     * {@link BaseEntityMapper#insert}
     */
    String METHOD_INSERT = "insert";
    /**
     * {@link BaseEntityMapper#batchInsert}
     */
    String METHOD_BATCH_INSERT = "batchInsert";
    /**
     * {@link BaseEntityMapper#deleteById}
     */
    String METHOD_DELETE_BY_ID = "deleteById";
    /**
     * {@link BaseEntityMapper#deleteByIds}
     */
    String METHOD_DELETE_BY_IDS = "deleteByIds";
    /**
     * {@link BaseEntityMapper#updateById}
     */
    String METHOD_UPDATE_BY_ID = "updateById";
    /**
     * {@link BaseEntityMapper#findById}
     */
    String METHOD_FIND_BY_ID = "findById";
    /**
     * {@link BaseEntityMapper#listByIds}
     */
    String METHOD_LIST_BY_IDS = "listByIds";
    /**
     * {@link BaseEntityMapper#listByCondition}
     */
    String METHOD_LIST_BY_CONDITION = "listByCondition";
    /**
     * {@link BaseEntityMapper#countByCondition}
     */
    String METHOD_COUNT_BY_CONDITION = "countByCondition";

}
