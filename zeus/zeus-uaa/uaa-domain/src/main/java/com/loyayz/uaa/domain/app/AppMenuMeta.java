package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.data.UaaMenu;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.uaa.dto.SimpleMenu;
import com.loyayz.zeus.AbstractEntity;
import com.loyayz.zeus.EntityId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loyayz.uaa.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class AppMenuMeta extends AbstractEntity<UaaAppMenuMeta, Long> {
    private final EntityId appId;

    private final List<String> deletedMenuActions = new ArrayList<>();

    static AppMenuMeta of(EntityId appId) {
        return of(appId, null);
    }

    static AppMenuMeta of(EntityId appId, Long menuId) {
        return new AppMenuMeta(appId, menuId);
    }

    public AppMenuMeta info(SimpleMenu menu) {
        if (!super.hasId()) {
            super.id().set(menu.getId());
        }
        if (super.hasId()) {
            UaaAppMenuMeta entity = super.entity();
            if (entity == null) {
                this.entity(menu);
            } else {
                AppConverter.setEntity(entity, menu);
            }
        } else {
            super.id().set(new UaaAppMenuMeta().genId());
            this.entity(menu);
        }
        menu.setId(super.idValue());
        super.markUpdated();
        return this;
    }

    private void entity(SimpleMenu menu) {
        if (menu.getPid() == null) {
            menu.setPid(ROOT_MENU_CODE);
        }
        UaaAppMenuMeta entity = AppConverter.toEntity(super.idValue(), menu);
        super.entity(entity);
    }

    public AppMenuMeta removeAction(String actionCode) {
        this.deletedMenuActions.add(actionCode);
        return this;
    }

    public AppMenuAction action(String actionCode) {
        return AppMenuAction.of(super.id(), actionCode);
    }

    @Override
    protected UaaAppMenuMeta buildEntity() {
        return super.hasId() ?
                AppRepository.getAppMenu(super.idValue()) : null;
    }

    @Override
    protected void fillEntityBeforeSave(UaaAppMenuMeta entity) {
        entity.setAppId(this.appId.get());
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByMenuAndCodes}
     */
    @Override
    protected void saveExtra() {
        if (this.deletedMenuActions.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("menuMetaId", super.idValue());
        param.put("actionCodes", this.deletedMenuActions);
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenuAndCodes", param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByMenu}
     * {@link com.loyayz.uaa.data.mapper.UaaMenuMapper#deleteByMeta}
     */
    @Override
    protected void deleteExtra() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("menuMetaId", super.idValue());
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenu", param);
        MybatisUtils.executeDelete(UaaMenu.class, "deleteByMeta", param);
    }

    private AppMenuMeta(EntityId appId, Long menuId) {
        super(menuId);
        this.appId = appId;
    }
}
