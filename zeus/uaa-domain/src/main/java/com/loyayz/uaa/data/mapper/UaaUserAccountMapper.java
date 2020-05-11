package com.loyayz.uaa.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseEntityMapper;
import com.loyayz.uaa.data.entity.UaaUserAccount;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UaaUserAccountMapper extends BaseEntityMapper<UaaUserAccount> {

    @Delete("DELETE FROM uaa_user_account " +
            "WHERE user_id = #{userId} AND type = #{type} AND name = #{name}")
    int deleteAccount(UaaUserAccount entity);

    @Update("UPDATE uaa_user_account SET password = #{entity.password} " +
            "WHERE user_id = #{userId} AND type = #{type} AND name = #{name}")
    int updatePassword(UaaUserAccount entity);

    @Delete("DELETE FROM uaa_user_account WHERE user_id = #{userId}")
    int deleteByUser(@Param("userId") Long userId);

    @Select("SELECT * FROM uaa_user_account WHERE type = #{type} AND name = #{name}")
    UaaUserAccount getAccount(@Param("type") String type, @Param("name") String name);

}
