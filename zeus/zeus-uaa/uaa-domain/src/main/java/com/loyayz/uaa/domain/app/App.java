package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.dto.SimpleMenu;
import com.loyayz.uaa.data.*;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class App extends AbstractEntity<UaaApp, Long> {
    private final AppHelper helper;

    public static App of() {
        return of(null);
    }

    public static App of(Long appId) {
        return new App(appId);
    }

    public App name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    public App url(String url) {
        super.entity().setUrl(url);
        super.markUpdated();
        return this;
    }

    public App remark(String remark) {
        super.entity().setRemark(remark);
        super.markUpdated();
        return this;
    }

    public App sort(int sort) {
        super.entity().setSort(sort);
        super.markUpdated();
        return this;
    }

    /**
     * 添加应用角色
     *
     * @param roleName 角色名
     */
    public App addRole(String roleName) {
        this.helper.addRole(roleName);
        return this;
    }

    /**
     * 添加菜单元数据
     *
     * @param pid  上级菜单
     * @param menu 菜单
     */
    public App addMenu(Long pid, SimpleMenu menu) {
        this.helper.addMenu(pid, menu);
        return this;
    }

    public AppMenuMeta menuMeta(Long menuMetaId) {
        return AppMenuMeta.of(super.id(), menuMetaId);
    }

    public AppMenuAction menuAction(Long menuMetaId, String actionCode) {
        return this.menuMeta(menuMetaId).action(actionCode);
    }

    @Override
    protected UaaApp buildEntity() {
        if (super.hasId()) {
            return AppRepository.findById(this.idValue());
        } else {
            UaaApp app = new UaaApp();
            app.setUrl("");
            app.setRemark("");
            return app;
        }
    }

    @Override
    protected void fillEntityBeforeSave(UaaApp entity) {
        if (entity.getSort() == null) {
            entity.setSort(AppRepository.getAppNextSort());
        }
    }

    @Override
    protected void saveExtra() {
        this.helper.save();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuMetaMapper#deleteByApp}
     * {@link com.loyayz.uaa.data.mapper.UaaMenuMapper#deleteByApp}
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMapper#deleteByApp}
     * {@link com.loyayz.uaa.data.mapper.UaaClientAppMapper#deleteByApp}
     */
    @Override
    protected void deleteExtra() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("appId", super.idValue());
        MybatisUtils.executeDelete(UaaAppMenuMeta.class, "deleteByApp", param);
        MybatisUtils.executeDelete(UaaMenu.class, "deleteByApp", param);
        MybatisUtils.executeDelete(UaaRole.class, "deleteByApp", param);
        MybatisUtils.executeDelete(UaaClientApp.class, "deleteByApp", param);
    }

    private App(Long appId) {
        super(appId);
        this.helper = AppHelper.of(super.id());
    }

}
