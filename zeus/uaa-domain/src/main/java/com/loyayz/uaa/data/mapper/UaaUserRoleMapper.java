package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseEntityMapper;
import com.loyayz.uaa.data.entity.UaaRole;
import com.loyayz.uaa.data.entity.UaaUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaUserRoleMapper extends BaseEntityMapper<UaaUserRole> {

    @Delete("<script>" +
            "DELETE FROM uaa_user_role WHERE user_id = #{userId}" +
            " AND role_code IN (" +
            "   <foreach collection=\"roleCodes\" item=\"roleCode\" separator=\",\">" +
            "       #{roleCode}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteRolesByUser(@Param("userId") Long userId, @Param("roleCodes") List<String> roleCodes);

    @Delete("<script>" +
            "DELETE FROM uaa_user_role WHERE role_code = #{roleCode}" +
            " AND user_id IN (" +
            "   <foreach collection=\"userIds\" item=\"userId\" separator=\",\">" +
            "       #{userId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteRoleByUsers(@Param("userIds") List<Long> userIds, @Param("roleCode") String roleCode);

    @Delete("DELETE FROM uaa_user_role WHERE user_id = #{userId}")
    int deleteByUser(@Param("userId") Long userId);

    @Delete("DELETE FROM uaa_user_role WHERE role_code = #{roleCode}")
    int deleteByRole(@Param("roleCode") String roleCode);

    @Select("SELECT r.* FROM uaa_user_role u,uaa_role r " +
            "  WHERE u.role_code = r.code AND u.user_id = #{userId} ")
    List<UaaRole> listByUser(@Param("userId") Long userId);

}
