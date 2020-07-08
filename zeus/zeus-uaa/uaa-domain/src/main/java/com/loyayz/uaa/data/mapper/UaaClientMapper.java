package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaClient;
import com.loyayz.uaa.dto.ClientQueryParam;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaClientMapper extends BaseMapper<UaaClient> {

    @Select("<script>SELECT * FROM uaa_client " +
            "   <where>" +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%',#{name},'%') " +
            "   </if>" +
            "</where> </script>")
    List<UaaClient> listByParam(ClientQueryParam queryParam);

}
