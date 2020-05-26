package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaRoleApp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaRoleActionMapper extends BaseMapper<UaaRoleApp> {

    @Delete("<script>" +
            "DELETE FROM uaa_user_action WHERE role_code = #{roleCode}" +
            " AND action_id IN (" +
            "   <foreach collection=\"actionIds\" item=\"actionId\" separator=\",\">" +
            "       #{actionId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByRoleActions(@Param("roleCode") String roleCode, @Param("actionIds") List<Long> actionIds);

    @Delete("DELETE FROM uaa_user_action WHERE role_code = #{roleCode}")
    int deleteByRole(@Param("roleCode") String roleCode);

}
