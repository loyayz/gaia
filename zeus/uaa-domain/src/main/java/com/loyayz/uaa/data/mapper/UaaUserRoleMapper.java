package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaAppRole;
import com.loyayz.uaa.data.UaaUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaUserRoleMapper extends BaseMapper<UaaUserRole> {

    @Delete("<script>" +
            "DELETE FROM uaa_user_role WHERE user_id = #{userId}" +
            " AND role_id IN (" +
            "   <foreach collection=\"roleIds\" item=\"roleId\" separator=\",\">" +
            "       #{roleId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    @Delete("<script>" +
            "DELETE FROM uaa_user_role WHERE role_id = #{roleId}" +
            " AND user_id IN (" +
            "   <foreach collection=\"userIds\" item=\"userId\" separator=\",\">" +
            "       #{userId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByUsersRole(@Param("userIds") List<Long> userIds, @Param("roleId") Long roleId);

    @Delete("DELETE FROM uaa_user_role WHERE user_id = #{userId}")
    int deleteByUser(@Param("userId") Long userId);

    @Delete("DELETE FROM uaa_user_role WHERE role_id = #{roleId}")
    int deleteByRole(@Param("roleId") Long roleId);

    @Select("SELECT r.* FROM uaa_user_role u,uaa_app_role r " +
            "  WHERE u.role_id = r.id AND u.user_id = #{userId} ")
    List<UaaAppRole> listByUser(@Param("userId") Long userId);

}
