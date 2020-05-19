package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaAppAdmin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaAppAdminMapper extends BaseMapper<UaaAppAdmin> {

    @Delete("<script>" +
            "DELETE FROM uaa_app_admin WHERE app_id = #{appId}" +
            " AND user_id IN (" +
            "   <foreach collection=\"userIds\" item=\"userId\" separator=\",\">" +
            "       #{userId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByAppUsers(@Param("appId") Long appId, @Param("userIds") List<Long> userIds);

    @Delete("DELETE FROM uaa_app_admin WHERE user_id = #{userId}")
    int deleteByUser(@Param("userId") Long userId);

}
