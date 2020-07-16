package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaOrg;
import com.loyayz.uaa.dto.OrgQueryParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaOrgMapper extends BaseMapper<UaaOrg> {

    @Select("SELECT MAX(sort) FROM uaa_org WHERE pid = #{pid}")
    Integer getMaxSortByParent(@Param("pid") Long pid);

    @Select("<script>SELECT * FROM uaa_org " +
            "   <where>" +
            "   <if test=\"pid != null\">" +
            "       pid = ${pid} " +
            "   </if>" +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%',#{name},'%') " +
            "   </if>" +
            "</where> ORDER BY sort DESC</script>")
    List<UaaOrg> listByParam(OrgQueryParam queryParam);

    @Select("SELECT * FROM uaa_org " +
            "   WHERE id IN (SELECT org_id FROM uaa_org_user WHERE user_id = #{userId}) " +
            "  ORDER BY sort DESC")
    List<UaaOrg> listByUser(@Param("userId") Long userId);

}
