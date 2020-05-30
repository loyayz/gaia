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

    @Delete("DELETE FROM uaa_app_menu_action WHERE menu_meta_id = #{menuMetaId}")
    int deleteByMenu(@Param("menuMetaId") Long menuMetaId);

    @Delete("<script>DELETE FROM uaa_app_menu_action " +
            " WHERE menu_meta_id = #{menuMetaId}" +
            " AND code IN (" +
            "   <foreach collection=\"actionCodes\" item=\"actionCode\" separator=\",\">" +
            "       #{actionCode}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByMenuAndCodes(@Param("menuMetaId") Long menuMetaId, @Param("actionCodes") List<String> actionCodes);

}
