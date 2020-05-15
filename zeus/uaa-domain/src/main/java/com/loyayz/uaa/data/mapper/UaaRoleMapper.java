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

    @Delete("UPDATE uaa_role SET name = #{name} WHERE code = #{code}")
    int updateByCode(UaaRole entity);

    @Delete("DELETE FROM uaa_role WHERE code = #{roleCode}")
    int deleteByCode(@Param("roleCode") String code);

    @Select("<script>SELECT * FROM uaa_role " +
            "   <where> " +
            "   <if test=\"code != null and code != ''\">" +
            "       code LIKE CONCAT('%',#{code},'%') " +
            "   </if>" +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%',#{name},'%') " +
            "   </if>" +
            "</where> </script>")
    List<UaaRole> listByParam(RoleQueryParam queryParam);

}
