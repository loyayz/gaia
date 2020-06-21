package com.loyayz.sys.data.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import com.loyayz.sys.data.SysSetting;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface SysSettingMapper extends BaseMapper<SysSetting> {

    @Delete("DELETE FROM sys_setting WHERE code = #{code}")
    int deleteByCode(@Param("code") String code);

}
