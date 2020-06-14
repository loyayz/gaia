package com.loyayz.uaa.domain.app;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.common.dto.SimpleMenu;
import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.data.UaaAppMenuMeta;
import com.loyayz.uaa.data.UaaMenu;
import com.loyayz.uaa.data.UaaRole;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class App extends AbstractEntity<UaaApp> {
    private final AppId appId;
    private AppHelper helper;

    public static App of() {
        return new App();
    }

    public static App of(Long appId) {
        return new App(appId);
    }

    public Long id() {
        return this.appId.get();
    }

    public App name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    public App remote(boolean remote) {
        super.entity().setRemote(remote ? 1 : 0);
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
        if (this.helper == null) {
            this.helper = AppHelper.of(this.appId);
        }
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
        if (this.helper == null) {
            this.helper = AppHelper.of(this.appId);
        }
        this.helper.addMenu(pid, menu);
        return this;
    }

    public AppMenuMeta menuMeta(Long menuMetaId) {
        return AppMenuMeta.of(this.appId, menuMetaId);
    }

    public AppMenuAction menuAction(Long menuMetaId, String actionCode) {
        return this.menuMeta(menuMetaId).action(actionCode);
    }

    @Override
    protected UaaApp buildEntity() {
        if (this.appId.isEmpty()) {
            UaaApp app = new UaaApp();
            app.setRemote(0);
            app.setUrl("");
            app.setSort(AppRepository.getAppNextSort());
            return app;
        } else {
            return AppRepository.findById(this.id());
        }
    }

    @Override
    public void save() {
        super.save();
        this.appId.set(super.entity().getId());

        if (this.helper != null) {
            this.helper.save();
        }
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuMetaMapper#deleteByApp}
     * {@link com.loyayz.uaa.data.mapper.UaaMenuMapper#deleteByApp}
     * {@link com.loyayz.uaa.data.mapper.UaaRoleMapper#deleteByApp}
     */
    @Override
    public void delete() {
        super.delete();

        Map<String, Object> param = new HashMap<>(2);
        param.put("appId", this.appId.get());
        MybatisUtils.executeDelete(UaaAppMenuMeta.class, "deleteByApp", param);
        MybatisUtils.executeDelete(UaaMenu.class, "deleteByApp", param);
        MybatisUtils.executeDelete(UaaRole.class, "deleteByApp", param);
    }

    private App() {
        this.appId = AppId.of();
    }

    private App(Long appId) {
        this.appId = AppId.of(appId);
    }

}
