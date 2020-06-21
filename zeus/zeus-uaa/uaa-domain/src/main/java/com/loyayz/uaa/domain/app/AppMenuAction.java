package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.zeus.AbstractEntity;
import com.loyayz.zeus.Identity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class AppMenuAction extends AbstractEntity<UaaAppMenuAction, String> {
    private final Identity menuId;

    static AppMenuAction of(Identity menuId, String code) {
        return new AppMenuAction(menuId, code);
    }

    public AppMenuAction name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    @Override
    protected UaaAppMenuAction buildEntity() {
        Long menuId = this.menuId.get();
        String actionCode = super.id();
        UaaAppMenuAction entity = AppRepository.getAppMenuActionByCode(menuId, actionCode);
        if (entity == null) {
            entity = AppConverter.toEntity(menuId, actionCode);
        }
        return entity;
    }

    @Override
    protected void fillEntityBeforeSave(UaaAppMenuAction entity) {
        entity.setMenuMetaId(this.menuId.get());
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByMenuAndCode}
     */
    @Override
    public void delete() {
        Map<String, Object> param = new HashMap<>(3);
        param.put("menuMetaId", this.menuId.get());
        param.put("actionCode", super.id());
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenuAndCode", param);
    }

    private AppMenuAction(Identity menuId, String code) {
        super(code);
        this.menuId = menuId;
    }

}
