package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaRoleApp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaRoleAppMapper extends BaseMapper<UaaRoleApp> {

    @Delete("<script>" +
            "DELETE FROM uaa_user_role WHERE role_code = #{roleCode}" +
            " AND app_id IN (" +
            "   <foreach collection=\"appIds\" item=\"appId\" separator=\",\">" +
            "       #{appId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByRoleApps(@Param("roleCode") String roleCode, @Param("appIds") List<Long> appIds);

    @Delete("DELETE FROM uaa_user_role WHERE role_code = #{roleCode}")
    int deleteByRole(@Param("roleCode") String roleCode);

}
