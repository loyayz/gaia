package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.data.UaaUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaUserRoleMapper extends BaseMapper<UaaUserRole> {

    @Delete("DELETE FROM uaa_user_role WHERE user_id = #{userId}")
    int deleteByUser(@Param("userId") Long userId);

    @Delete("DELETE FROM uaa_user_role WHERE role_id = #{roleId}")
    int deleteByRole(@Param("roleId") Long roleId);

    @Delete("DELETE FROM uaa_user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    int deleteByEntityRelation(UaaUserRole entity);

    @Select("<script>" +
            "SELECT user_id FROM uaa_user_role WHERE role_id = #{roleId}" +
            " AND user_id IN (" +
            "   <foreach collection=\"userIds\" item=\"userId\" separator=\",\">" +
            "       #{userId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    List<Long> listUserByRoleUsers(@Param("roleId") Long roleId, @Param("userIds") List<Long> userIds);

    @Select("<script>" +
            "SELECT role_id FROM uaa_user_role WHERE user_id = #{userId}" +
            " AND role_id IN (" +
            "   <foreach collection=\"roleIds\" item=\"roleId\" separator=\",\">" +
            "       #{roleId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    List<Long> listRoleByUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    @Select("SELECT r.* FROM uaa_user_role u,uaa_role r " +
            "  WHERE u.role_id = r.id AND u.user_id = #{userId} ")
    List<UaaRole> listByUser(@Param("userId") Long userId);

}
