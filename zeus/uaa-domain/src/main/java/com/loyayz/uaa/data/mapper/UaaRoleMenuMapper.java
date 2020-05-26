package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaRoleApp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaRoleMenuMapper extends BaseMapper<UaaRoleApp> {

    @Delete("<script>" +
            "DELETE FROM uaa_user_menu WHERE role_code = #{roleCode}" +
            " AND menu_id IN (" +
            "   <foreach collection=\"menuIds\" item=\"menuId\" separator=\",\">" +
            "       #{menuId}" +
            "   </foreach>" +
            "   )" +
            "</script>")
    int deleteByRoleMenus(@Param("roleCode") String roleCode, @Param("menuIds") List<Long> menuIds);

    @Delete("DELETE FROM uaa_user_menu WHERE role_code = #{roleCode}")
    int deleteByRole(@Param("roleCode") String roleCode);

}
