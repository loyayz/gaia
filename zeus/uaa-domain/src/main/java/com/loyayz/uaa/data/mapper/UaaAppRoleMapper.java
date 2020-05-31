package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.common.dto.RoleQueryParam;
import com.loyayz.uaa.data.UaaAppRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaAppRoleMapper extends BaseMapper<UaaAppRole> {

    @Delete("DELETE FROM uaa_app_role WHERE app_id = #{appId}")
    int deleteByApp(@Param("appId") Long appId);

    @Select("<script>SELECT * FROM uaa_app_role " +
            "   <where> " +
            "   <if test=\"appId != null\">" +
            "       app_id = #{appId} " +
            "   </if>" +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%',#{name},'%') " +
            "   </if>" +
            "</where> </script>")
    List<UaaAppRole> listByParam(RoleQueryParam queryParam);

}
