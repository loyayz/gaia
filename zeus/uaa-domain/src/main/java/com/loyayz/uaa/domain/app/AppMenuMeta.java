package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.dto.SimpleMenu;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.data.UaaMenu;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.zeus.AbstractEntity;
import com.loyayz.zeus.Identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loyayz.uaa.common.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class AppMenuMeta extends AbstractEntity<UaaAppMenuMeta, Long> {
    private final Identity appId;

    private final List<String> deletedMenuActions = new ArrayList<>();

    static AppMenuMeta of(Identity appId) {
        return of(appId, null);
    }

    static AppMenuMeta of(Identity appId, Long menuId) {
        return new AppMenuMeta(appId, menuId);
    }

    public AppMenuMeta info(SimpleMenu menu) {
        if (super.idIsEmpty()) {
            super.identity().set(menu.getId());
        }
        if (super.idIsEmpty()) {
            super.identity().set(new UaaAppMenuMeta().genId());
            this.entity(menu);
        } else {
            UaaAppMenuMeta entity = super.entity();
            if (entity == null) {
                this.entity(menu);
            } else {
                AppConverter.setEntity(entity, menu);
            }
        }
        menu.setId(super.id());
        super.markUpdated();
        return this;
    }

    private void entity(SimpleMenu menu) {
        if (menu.getPid() == null) {
            menu.setPid(ROOT_MENU_CODE);
        }
        UaaAppMenuMeta entity = AppConverter.toEntity(super.id(), menu);
        super.entity(entity);
    }

    public AppMenuMeta removeAction(String actionCode) {
        this.deletedMenuActions.add(actionCode);
        return this;
    }

    public AppMenuAction action(String actionCode) {
        return AppMenuAction.of(super.identity(), actionCode);
    }

    @Override
    protected UaaAppMenuMeta buildEntity() {
        return super.idIsEmpty() ?
                null : AppRepository.getAppMenu(super.id());
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
        param.put("menuMetaId", super.id());
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
        param.put("menuMetaId", super.id());
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenu", param);
        MybatisUtils.executeDelete(UaaMenu.class, "deleteByMeta", param);
    }

    private AppMenuMeta(Identity appId, Long menuId) {
        super(menuId);
        this.appId = appId;
    }
}
