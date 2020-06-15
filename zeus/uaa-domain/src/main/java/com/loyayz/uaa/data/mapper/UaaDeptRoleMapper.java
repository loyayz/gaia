package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaDeptRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaDeptRoleMapper extends BaseMapper<UaaDeptRole> {

    @Delete("DELETE FROM uaa_dept_role WHERE dept_id = #{deptId}")
    int deleteByDept(@Param("deptId") Long deptId);

    @Delete("<script>" +
            "DELETE FROM uaa_dept_role WHERE dept_id = #{deptId}" +
            " AND role_id IN (" +
            "   <foreach collection=\"roleIds\" item=\"roleId\" separator=\",\">" +
            "       #{roleId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByDeptRoles(@Param("deptId") Long deptId, @Param("roleIds") List<Long> roleIds);

    @Select("<script>" +
            "SELECT role_id FROM uaa_dept_role WHERE dept_id = #{deptId}" +
            " AND role_id IN (" +
            "   <foreach collection=\"roleIds\" item=\"roleId\" separator=\",\">" +
            "       #{roleId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    List<Long> listRoleByDeptRoles(@Param("deptId") Long deptId, @Param("roleIds") List<Long> roleIds);

}
