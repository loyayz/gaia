package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.data.UaaAppMenu;
import com.loyayz.uaa.dto.MenuQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaAppMenuMapper extends BaseMapper<UaaAppMenu> {

    @Delete("DELETE FROM uaa_app_menu WHERE app_id = #{appId}")
    int deleteByApp(@Param("appId") Long appId);

    @Delete("DELETE FROM uaa_app_menu WHERE app_id = #{appId} AND code = #{menuCode}")
    int deleteByCode(@Param("appId") Long appId, @Param("menuCode") String code);

    @Select("<script>SELECT * FROM uaa_app_menu " +
            "   <where> " +
            "   <if test=\"appId != null\">" +
            "       app_id = #{appId} " +
            "   </if>" +
            "   <if test=\"parentCode != null and parentCode != ''\">" +
            "       AND parent_code = #{parentCode} " +
            "   </if>" +
            "   <if test=\"code != null and code != ''\">" +
            "       AND code LIKE CONCAT('%',#{code},'%') " +
            "   </if>" +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%',#{name},'%') " +
            "   </if>" +
            "   <if test=\"url != null and url != ''\">" +
            "       AND url LIKE CONCAT('%',#{url},'%') " +
            "   </if>" +
            "</where> ORDER BY sort </script>")
    List<UaaApp> listByParam(MenuQueryParam queryParam);

}
