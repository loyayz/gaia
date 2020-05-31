package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.dto.SimpleMenu;
import com.loyayz.uaa.data.UaaAppMenuAction;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.data.UaaMenu;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.*;

import static com.loyayz.uaa.common.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class AppMenuMeta extends AbstractEntity<UaaAppMenuMeta> {
    private final AppId appId;
    private Long menuId;

    private final List<String> deletedMenuActions = new ArrayList<>();

    static AppMenuMeta of(AppId appId) {
        return of(appId, null);
    }

    static AppMenuMeta of(AppId appId, Long menuId) {
        return new AppMenuMeta(appId, menuId);
    }

    public Long id() {
        return this.menuId;
    }

    public AppMenuMeta info(SimpleMenu menu) {
        if (this.menuId == null) {
            this.menuId = menu.getId();
        }
        if (this.menuId == null) {
            this.menuId = new UaaAppMenuMeta().genId();
            this.entity(menu);
        } else {
            UaaAppMenuMeta entity = super.entity();
            if (entity == null) {
                this.entity(menu);
            } else {
                AppConverter.setEntity(entity, menu);
            }
        }
        menu.setId(this.menuId);
        super.markUpdated();
        return this;
    }

    private void entity(SimpleMenu menu) {
        if (menu.getPid() == null) {
            menu.setPid(ROOT_MENU_CODE);
        }
        UaaAppMenuMeta entity = AppConverter.toEntity(this.menuId, menu);
        super.entity(entity);
    }

    public AppMenuMeta removeAction(String... actionCodes) {
        return this.removeAction(Arrays.asList(actionCodes));
    }

    public AppMenuMeta removeAction(List<String> actionCodes) {
        this.deletedMenuActions.addAll(actionCodes);
        return this;
    }

    public AppMenuAction action(String actionCode) {
        return AppMenuAction.of(this.id(), actionCode);
    }

    @Override
    protected UaaAppMenuMeta buildEntity() {
        return this.menuId == null ?
                null : AppRepository.getAppMenu(menuId);
    }

    @Override
    public void save() {
        if (super.updated()) {
            UaaAppMenuMeta entity = super.entity();
            entity.setAppId(this.appId.get());
            entity.save();
        }
        this.saveActions();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByMenu}
     * {@link com.loyayz.uaa.data.mapper.UaaMenuMapper#deleteByMeta}
     */
    @Override
    public void delete() {
        new UaaAppMenuMeta().deleteById(this.id());

        Map<String, Object> param = new HashMap<>(2);
        param.put("menuMetaId", this.id());
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenu", param);
        MybatisUtils.executeDelete(UaaMenu.class, "deleteByMeta", param);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByMenuAndCodes}
     */
    private void saveActions() {
        if (this.deletedMenuActions.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("menuMetaId", this.id());
        param.put("actionCodes", this.deletedMenuActions);
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenuAndCodes", param);
    }

    private AppMenuMeta(AppId appId, Long menuId) {
        this.appId = appId;
        this.menuId = menuId;
    }
}
