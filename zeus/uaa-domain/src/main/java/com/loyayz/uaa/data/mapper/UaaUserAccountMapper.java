package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.uaa.data.UaaUserAccount;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaUserAccountMapper extends BaseMapper<UaaUserAccount> {

    @Delete("DELETE FROM uaa_user_account " +
            "WHERE user_id = #{userId} AND type = #{type} AND name = #{name}")
    int deleteAccount(@Param("userId") Long userId, @Param("type") String type, @Param("name") String name);

    @Delete("DELETE FROM uaa_user_account WHERE user_id = #{userId}")
    int deleteByUser(@Param("userId") Long userId);

    @Select("SELECT * FROM uaa_user_account WHERE type = #{type} AND name = #{name}")
    UaaUserAccount getAccount(@Param("type") String type, @Param("name") String name);

}
