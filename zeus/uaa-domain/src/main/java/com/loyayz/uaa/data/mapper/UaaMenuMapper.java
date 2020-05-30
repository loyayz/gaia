package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaMenuMapper extends BaseMapper<UaaMenu> {

    @Delete("DELETE FROM uaa_menu WHERE app_id = #{appId}")
    int deleteByApp(@Param("appId") Long appId);

    @Delete("DELETE FROM uaa_menu WHERE menu_meta_id = #{menuMetaId}")
    int deleteByMeta(@Param("menuMetaId") Long menuMetaId);

    @Select("SELECT MAX(sort) FROM uaa_menu WHERE pid = #{pid}")
    Integer getMaxSortByParent(@Param("pid") Long pid);

}
