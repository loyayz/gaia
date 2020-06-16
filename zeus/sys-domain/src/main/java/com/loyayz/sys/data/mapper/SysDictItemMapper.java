package com.loyayz.sys.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.sys.data.SysDictItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    @Delete("DELETE FROM sys_dict_item WHERE dict_code = #{dictCode}")
    int deleteByCode(@Param("dictCode") String dictCode);

    @Delete("<script>" +
            "DELETE FROM sys_dict_item WHERE dict_code = #{dictCode}" +
            " AND id IN (" +
            "   <foreach collection=\"ids\" item=\"id\" separator=\",\">" +
            "       #{id}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByCodeItems(@Param("dictCode") String dictCode, @Param("ids") List<Long> ids);

    @Select("SELECT MAX(sort) FROM sys_dict_item WHERE dict_code = #{dictCode}")
    Integer getMaxSortByCode(@Param("dictCode") String dictCode);

}
