package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseEntityMapper;
import com.loyayz.uaa.data.entity.UaaAppMenuAction;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaAppMenuActionMapper extends BaseEntityMapper<UaaAppMenuAction> {

    @Delete("DELETE FROM uaa_app_menu_action WHERE app_id = #{appId}")
    int deleteByApp(@Param("appId") Long appId);

    @Delete("DELETE FROM uaa_app_menu_action WHERE app_id = #{appId} AND menu_code = #{menuCode}")
    int deleteByMenu(@Param("appId") Long appId, @Param("menuCode") String menuCode);

    @Delete("<script>DELETE FROM uaa_app_menu_action " +
            " WHERE app_id = #{appId} AND menu_code = #{menuCode}" +
            " AND code IN (" +
            "   <foreach collection=\"actionCodes\" item=\"actionCode\" separator=\",\">" +
            "       #{actionCode}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByCode(@Param("appId") Long appId, @Param("menuCode") String menuCode, @Param("actionCodes") List<String> actionCodes);

}