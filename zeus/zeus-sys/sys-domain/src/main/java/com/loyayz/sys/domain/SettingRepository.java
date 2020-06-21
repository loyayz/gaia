package com.loyayz.sys.domain;

import com.loyayz.sys.data.SysSetting;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class SettingRepository {

    public static SysSetting findByCode(String dictCode) {
        List<SysSetting> settings = SysSetting.builder().code(dictCode).build().listByCondition();
        return settings.isEmpty() ? null : settings.get(0);
    }

}
