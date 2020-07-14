package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.dto.AppQueryParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaAppMapper extends BaseMapper<UaaApp> {

    @Select("SELECT MAX(sort) FROM uaa_app")
    Integer getMaxSort();

    @Select("<script>SELECT * FROM uaa_app " +
            "  <where> " +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%',#{name},'%') " +
            "   </if>" +
            "  </where> ORDER BY sort DESC </script>")
    List<UaaApp> listByParam(AppQueryParam queryParam);

    @Select("SELECT * FROM uaa_app WHERE id IN ( " + UaaRoleMapper.SELECT_APP_BY_USER + ") ORDER BY sort DESC")
    List<UaaApp> listByUser(@Param("userId") Long userId);

}
