package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaAppMenuAction;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaAppMenuActionMapper extends BaseMapper<UaaAppMenuAction> {

    @Delete("DELETE FROM uaa_app_menu_action WHERE app_id = #{appId}")
    int deleteByApp(@Param("appId") Long appId);

    @Delete("<script>DELETE FROM uaa_app_menu_action WHERE " +
            " menu_code IN (" +
            "   <foreach collection=\"menuCodes\" item=\"menuCode\" separator=\",\">" +
            "       #{menuCode}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByMenus(@Param("menuCodes") List<String> menuCodes);

    @Delete("<script>DELETE FROM uaa_app_menu_action " +
            " WHERE menu_code = #{menuCode}" +
            " AND code IN (" +
            "   <foreach collection=\"actionCodes\" item=\"actionCode\" separator=\",\">" +
            "       #{actionCode}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByMenuAndCodes(@Param("menuCode") String menuCode, @Param("actionCodes") List<String> actionCodes);

}
