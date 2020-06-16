package com.loyayz.sys.domain.setting;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.sys.data.SysSetting;
import com.loyayz.sys.domain.SettingRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Setting extends AbstractEntity<SysSetting> {
    private final SettingId id;

    public static Setting of(String code) {
        return new Setting(code);
    }

    public String id() {
        return this.id.get();
    }

    public Setting name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    public Setting value(String value) {
        super.entity().setValue(value);
        super.markUpdated();
        return this;
    }

    @Override
    protected SysSetting buildEntity() {
        SysSetting entity = SettingRepository.findByCode(this.id());
        if (entity == null) {
            entity = new SysSetting();
            entity.setCode(this.id());
        }
        return entity;
    }

    /**
     * {@link com.loyayz.sys.data.mapper.SysSettingMapper#deleteByCode}
     */
    @Override
    public void delete() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("code", this.id.get());

        MybatisUtils.executeDelete(SysSetting.class, "deleteByCode", param);
    }

    private Setting(String code) {
        this.id = SettingId.of(code);
    }

}
