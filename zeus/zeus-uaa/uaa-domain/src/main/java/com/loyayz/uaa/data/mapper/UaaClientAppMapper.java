package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaClientApp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaClientAppMapper extends BaseMapper<UaaClientApp> {

    @Delete("DELETE FROM uaa_client_app WHERE client_id = #{clientId}")
    int deleteByClient(@Param("clientId") Long clientId);

    @Delete("DELETE FROM uaa_client_app WHERE app_id = #{appId}")
    int deleteByApp(@Param("appId") Long appId);

    @Delete("DELETE FROM uaa_client_app WHERE client_id = #{clientId} AND app_id = ${appId}")
    int deleteByEntityRelation(UaaClientApp entity);

    @Select("SELECT app_id FROM uaa_client_app WHERE client_id = #{clientId}")
    List<Long> listAppByClient(@Param("clientId") Long clientId);

    @Select("<script>" +
            "SELECT app_id FROM uaa_client_app WHERE client_id = #{clientId}" +
            " AND app_id IN (" +
            "   <foreach collection=\"appIds\" item=\"appId\" separator=\",\">" +
            "       #{appId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    List<Long> listAppByClientApps(@Param("clientId") Long clientId, @Param("appIds") List<Long> appIds);

}
