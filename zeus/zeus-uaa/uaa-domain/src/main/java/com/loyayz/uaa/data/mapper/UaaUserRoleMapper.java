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

    @Delete("<script>" +
            "DELETE FROM uaa_user_role WHERE role_id = #{roleId}" +
            " AND user_id IN (" +
            "   <foreach collection=\"userIds\" item=\"userId\" separator=\",\">" +
            "       #{userId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByRoleUsers(@Param("roleId") Long roleId, @Param("userIds") List<Long> userIds);

    @Delete("DELETE FROM uaa_user_role WHERE user_id = #{userId}")
    int deleteByUser(@Param("userId") Long userId);

    @Delete("DELETE FROM uaa_user_role WHERE role_id = #{roleId}")
    int deleteByRole(@Param("roleId") Long roleId);

    @Select("<script>" +
            "SELECT user_id FROM uaa_user_role WHERE role_id = #{roleId}" +
            " AND user_id IN (" +
            "   <foreach collection=\"userIds\" item=\"userId\" separator=\",\">" +
            "       #{userId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    List<Long> listUserByRoleUsers(@Param("roleId") Long roleId, @Param("userIds") List<Long> userIds);

    @Select("SELECT r.* FROM uaa_user_role u,uaa_role r " +
            "  WHERE u.role_id = r.id AND u.user_id = #{userId} ")
    List<UaaRole> listByUser(@Param("userId") Long userId);

}