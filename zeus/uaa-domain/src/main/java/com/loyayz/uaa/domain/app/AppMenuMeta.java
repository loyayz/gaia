package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.dto.SimpleMenu;
import com.loyayz.uaa.common.dto.SimpleMenuAction;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.data.UaaMenu;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loyayz.uaa.common.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppMenuMeta extends AbstractEntity<UaaAppMenuMeta> {
    private final AppId appId;
    private SimpleMenu menu;

    private final Map<String, AppMenuAction> menuActions = new HashMap<>();
    private final List<String> deletedMenuActions = new ArrayList<>();

    static AppMenuMeta of(AppId appId, Long menuId) {
        SimpleMenu menu = new SimpleMenu();
        menu.setId(menuId);
        return new AppMenuMeta(appId, menu);
    }

    static AppMenuMeta of(AppId appId, SimpleMenu menu) {
        AppMenuMeta meta = new AppMenuMeta(appId, menu);
        meta.markUpdated();
        return meta;
    }

    Long id() {
        return super.entity().getId();
    }

    void info(SimpleMenu menu) {
        this.menu = menu;
        super.markUpdated();
    }

    void addAction(List<SimpleMenuAction> actions) {
        Long menuId = this.id();
        for (SimpleMenuAction action : actions) {
            this.menuActions.put(action.getCode(), AppMenuAction.of(menuId, action));
        }
    }

    void removeAction(List<String> actionCodes) {
        for (String actionCode : actionCodes) {
            this.menuActions.remove(actionCode);
        }
        this.deletedMenuActions.addAll(actionCodes);
    }

    @Override
    protected UaaAppMenuMeta buildEntity() {
        if (this.menu.getPid() == null) {
            this.menu.setPid(ROOT_MENU_CODE);
        }
        UaaAppMenuMeta entity = null;
        if (this.menu.getId() == null) {
            this.menu.setId(new UaaAppMenuMeta().genId());
        } else {
            entity = AppRepository.getAppMenu(this.menu.getId());
        }
        if (entity == null) {
            entity = AppConverter.toEntity(this.menu);
        } else {
            AppConverter.setEntity(entity, this.menu);
        }
        return entity;
    }

    @Override
    public void save() {
        super.entity().setAppId(this.appId.get());
        super.save();

        this.saveActions();
        this.deleteActions();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByMenu}
     * {@link com.loyayz.uaa.data.mapper.UaaMenuMapper#deleteByMeta}
     */
    @Override
    public void delete() {
        super.delete();

        Map<String, Object> param = new HashMap<>(2);
        param.put("menuMetaId", this.id());
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenu", param);
        MybatisUtils.executeDelete(UaaMenu.class, "deleteByMeta", param);
    }

    private void saveActions() {
        if (this.menuActions.isEmpty()) {
            return;
        }
        this.menuActions.forEach((actionCode, action) -> action.save());
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByMenuAndCodes}
     */
    private void deleteActions() {
        if (this.deletedMenuActions.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("menuMetaId", this.id());
        param.put("actionCodes", this.deletedMenuActions);
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenuAndCodes", param);
    }

    private AppMenuMeta(AppId appId, SimpleMenu menu) {
        this.appId = appId;
        this.menu = menu;
    }
}
