package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.dto.RoleQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaRoleMapper extends BaseMapper<UaaRole> {
    String FROM_TABLE_BY_USER = " FROM uaa_role WHERE id IN (" +
            " SELECT role_id FROM uaa_org_role WHERE org_id IN (SELECT org_id FROM uaa_org_user WHERE user_id = #{userId})" +
            " UNION SELECT role_id FROM uaa_user_role WHERE user_id = #{userId} )";
    String SELECT_APP_BY_USER = " SELECT app_id " + FROM_TABLE_BY_USER;

    @Delete("DELETE FROM uaa_role WHERE app_id = #{appId}")
    int deleteByApp(@Param("appId") Long appId);

    @Select("<script>SELECT * FROM uaa_role " +
            "   <where> app_id = #{appId} " +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%',#{name},'%') " +
            "   </if>" +
            "</where> </script>")
    List<UaaRole> listByParam(RoleQueryParam queryParam);

}
