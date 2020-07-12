package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaOrgRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaOrgRoleMapper extends BaseMapper<UaaOrgRole> {

    @Delete("DELETE FROM uaa_org_role WHERE org_id = #{orgId}")
    int deleteByOrg(@Param("orgId") Long orgId);

    @Delete("DELETE FROM uaa_org_role WHERE org_id = #{orgId} AND role_id = #{roleId}")
    int deleteByEntityRelation(UaaOrgRole entity);

    @Select("<script>" +
            "SELECT role_id FROM uaa_org_role WHERE org_id = #{orgId}" +
            " AND role_id IN (" +
            "   <foreach collection=\"roleIds\" item=\"roleId\" separator=\",\">" +
            "       #{roleId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    List<Long> listRoleByOrgRoles(@Param("orgId") Long orgId, @Param("roleIds") List<Long> roleIds);

    @Select("<script>" +
            "SELECT org_id FROM uaa_org_role WHERE role_id = #{roleId}" +
            " AND org_id IN (" +
            "   <foreach collection=\"orgIds\" item=\"orgId\" separator=\",\">" +
            "       #{orgId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    List<Long> listOrgByRoleOrgs(@Param("roleId") Long roleId, @Param("orgIds") List<Long> orgIds);

}
