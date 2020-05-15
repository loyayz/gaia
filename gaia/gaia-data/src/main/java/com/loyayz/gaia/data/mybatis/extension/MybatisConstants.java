package com.loyayz.gaia.data.mybatis.extension;

import com.loyayz.gaia.data.mybatis.BaseMapper;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface MybatisConstants {

    String CONDITION_SORTER = "sc";
    String CONDITION_SORTER_ITEM = "sci";

    /**
     * {@link BaseMapper#insert}
     */
    String METHOD_INSERT = "insert";
    /**
     * {@link BaseMapper#batchInsert}
     */
    String METHOD_BATCH_INSERT = "batchInsert";
    /**
     * {@link BaseMapper#deleteById}
     */
    String METHOD_DELETE_BY_ID = "deleteById";
    /**
     * {@link BaseMapper#deleteByIds}
     */
    String METHOD_DELETE_BY_IDS = "deleteByIds";
    /**
     * {@link BaseMapper#updateById}
     */
    String METHOD_UPDATE_BY_ID = "updateById";
    /**
     * {@link BaseMapper#findById}
     */
    String METHOD_FIND_BY_ID = "findById";
    /**
     * {@link BaseMapper#listByIds}
     */
    String METHOD_LIST_BY_IDS = "listByIds";
    /**
     * {@link BaseMapper#listByCondition}
     */
    String METHOD_LIST_BY_CONDITION = "listByCondition";
    /**
     * {@link BaseMapper#countByCondition}
     */
    String METHOD_COUNT_BY_CONDITION = "countByCondition";

}
