package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaDeptUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaDeptUserMapper extends BaseMapper<UaaDeptUser> {

    @Delete("DELETE FROM uaa_dept_user WHERE dept_id = #{deptId}")
    int deleteByDept(@Param("deptId") Long deptId);

    @Delete("<script>" +
            "DELETE FROM uaa_dept_user WHERE dept_id = #{deptId}" +
            " AND user_id IN (" +
            "   <foreach collection=\"userIds\" item=\"userId\" separator=\",\">" +
            "       #{userId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByDeptUsers(@Param("deptId") Long deptId, @Param("userIds") List<Long> userIds);

    @Select("<script>" +
            "SELECT user_id FROM uaa_dept_user WHERE dept_id = #{deptId}" +
            " AND user_id IN (" +
            "   <foreach collection=\"userIds\" item=\"userId\" separator=\",\">" +
            "       #{userId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    List<Long> listUserByDeptUsers(@Param("deptId") Long deptId, @Param("userIds") List<Long> userIds);

}
