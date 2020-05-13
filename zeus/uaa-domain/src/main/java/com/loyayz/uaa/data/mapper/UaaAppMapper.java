package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseEntityMapper;
import com.loyayz.uaa.data.entity.UaaApp;
import com.loyayz.uaa.dto.AppQueryParam;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaAppMapper extends BaseEntityMapper<UaaApp> {

    @Select("<script>SELECT * FROM uaa_app " +
            "   <where> " +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%',#{name},'%') " +
            "   </if>" +
            "   <if test=\"remote != null\">" +
            "       AND remote = #{remote} " +
            "   </if>" +
            "</where> </script>")
    List<UaaApp> listByParam(AppQueryParam queryParam);

}