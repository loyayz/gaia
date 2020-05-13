package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseEntityMapper;
import com.loyayz.uaa.data.UaaUser;
import com.loyayz.uaa.dto.UserQueryParam;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaUserMapper extends BaseEntityMapper<UaaUser> {

    @Select("<script>SELECT * FROM uaa_user " +
            "   WHERE deleted = 0 " +
            "   <if test=\"name != null and name != ''\">" +
            "       AND name LIKE CONCAT('%',#{name},'%') " +
            "   </if>" +
            "   <if test=\"mobile != null and mobile != ''\">" +
            "       AND mobile LIKE CONCAT('%',#{mobile},'%') " +
            "   </if>" +
            "   <if test=\"email != null and email != ''\">" +
            "       AND email LIKE CONCAT('%',#{email},'%') " +
            "   </if>" +
            "   <if test=\"locked != null \">" +
            "       AND locked = #{locked} " +
            "   </if>" +
            "   <if test=\"roleCode != null and roleCode != ''\">" +
            "       AND id IN (SELECT user_id FROM uaa_user_role WHERE role_code = #{roleCode}) " +
            "   </if>" +
            "</script>")
    List<UaaUser> listByParam(UserQueryParam queryParam);

}
