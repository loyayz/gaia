package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaRolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaRolePermissionMapper extends BaseMapper<UaaRolePermission> {

    @Delete("DELETE FROM uaa_role_permission WHERE role_id = #{roleId}")
    int deleteByRole(@Param("roleId") Long roleId);

    @Delete("<script>" +
            "DELETE FROM uaa_role_permission WHERE role_id = #{roleId} AND type = #{type} " +
            " AND ref_id IN (" +
            "   <foreach collection=\"refIds\" item=\"refId\" separator=\",\">" +
            "       #{refId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByRoleTypeRefs(@Param("roleId") Long roleId, @Param("type") String type, @Param("refIds") List<Long> refIds);

}
