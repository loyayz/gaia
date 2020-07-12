package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaOrgUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaOrgUserMapper extends BaseMapper<UaaOrgUser> {

    @Delete("DELETE FROM uaa_org_user WHERE org_id = #{orgId}")
    int deleteByOrg(@Param("orgId") Long orgId);

    @Delete("DELETE FROM uaa_org_user WHERE org_id = #{orgId} AND user_id = #{userId}")
    int deleteByEntityRelation(UaaOrgUser entity);

    @Select("<script>" +
            "SELECT user_id FROM uaa_org_user WHERE org_id = #{orgId}" +
            " AND user_id IN (" +
            "   <foreach collection=\"userIds\" item=\"userId\" separator=\",\">" +
            "       #{userId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    List<Long> listUserByOrgUsers(@Param("orgId") Long orgId, @Param("userIds") List<Long> userIds);

    @Select("<script>" +
            "SELECT org_id FROM uaa_org_user WHERE user_id = #{userId}" +
            " AND org_id IN (" +
            "   <foreach collection=\"orgIds\" item=\"orgId\" separator=\",\">" +
            "       #{orgId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    List<Long> listOrgByUserOrgs(@Param("userId") Long userId, @Param("orgIds") List<Long> orgIds);

}
