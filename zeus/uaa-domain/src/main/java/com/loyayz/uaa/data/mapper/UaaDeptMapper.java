package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaDept;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaDeptMapper extends BaseMapper<UaaDept> {

    @Select("SELECT MAX(sort) FROM uaa_dept WHERE pid = #{pid}")
    Integer getMaxSortByParent(@Param("pid") Long pid);

}
