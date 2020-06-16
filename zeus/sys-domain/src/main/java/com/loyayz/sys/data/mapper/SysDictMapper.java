package com.loyayz.sys.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.sys.data.SysDict;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    @Delete("DELETE FROM sys_dict WHERE code = #{dictCode}")
    int deleteByCode(@Param("dictCode") String dictCode);

    @Select("SELECT MAX(sort) FROM sys_dict WHERE group_name = #{groupName}")
    Integer getMaxSortByGroup(@Param("groupName") String groupName);

}
