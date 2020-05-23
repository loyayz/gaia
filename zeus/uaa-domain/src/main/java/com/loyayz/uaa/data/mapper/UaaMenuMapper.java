package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaMenuMapper extends BaseMapper<UaaMenu> {

    @Delete("DELETE FROM uaa_menu WHERE app_id = #{appId}")
    int deleteByApp(@Param("appId") Long appId);

    @Delete("<script>" +
            "DELETE FROM uaa_menu WHERE code IN (" +
            "   <foreach collection=\"menuCodes\" item=\"code\" separator=\",\">" +
            "       #{code}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByCodes(@Param("menuCodes") List<String> codes);

    @Select("SELECT MAX(sort) FROM uaa_menu WHERE parent_id = #{parentId}")
    Integer getMaxSortByParent(@Param("parentId") Long parentId);

}
