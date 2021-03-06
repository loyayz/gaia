package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.dto.MenuQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaAppMenuMetaMapper extends BaseMapper<UaaAppMenuMeta> {

    @Delete("DELETE FROM uaa_app_menu_meta WHERE app_id = #{appId}")
    int deleteByApp(@Param("appId") Long appId);

    @Select("<script>SELECT * FROM uaa_app_menu_meta " +
            "   <where> " +
            "   <if test=\"appId != null\">" +
            "       app_id = #{appId} " +
            "   </if>" +
            "   <if test=\"pid != null\">" +
            "       AND pid = #{pid} " +
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
    List<UaaAppMenuMeta> listByParam(MenuQueryParam queryParam);

}
