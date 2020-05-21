package com.loyayz.uaa.domain.app;

import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.uaa.dto.SimpleMenu;
import com.loyayz.zeus.AbstractEntity;

import java.util.Arrays;
import java.util.List;

import static com.loyayz.uaa.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class App extends AbstractEntity<UaaApp> {
    private final AppId appId;
    private AppAdmins appAdmins;
    private AppMenuHelper menuHelper;

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
     * 是否管理员
     *
     * @param userId 用户id
     */
    public boolean isAdmin(Long userId) {
        if (appAdmins == null) {
            appAdmins = AppAdmins.of(this.appId);
        }
        return appAdmins.containsUser(userId);
    }

    /**
     * 添加管理员
     *
     * @param userIds 用户id
     */
    public App addAdmin(Long... userIds) {
        return this.addAdmin(Arrays.asList(userIds));
    }

    public App addAdmin(List<Long> userIds) {
        if (appAdmins == null) {
            appAdmins = AppAdmins.of(this.appId);
        }
        appAdmins.addUsers(userIds);
        return this;
    }

    /**
     * 删除管理员
     *
     * @param userIds 用户id
     */
    public App removeAdmin(Long... userIds) {
        return this.removeAdmin(Arrays.asList(userIds));
    }

    public App removeAdmin(List<Long> userIds) {
        if (appAdmins == null) {
            appAdmins = AppAdmins.of(this.appId);
        }
        appAdmins.removeUsers(userIds);
        return this;
    }

    /**
     * 添加菜单元数据
     * 根据菜单编码查询元数据，存在则修改，否则新增
     *
     * @param parentCode 上级菜单编码
     * @param menus      菜单目录
     */
    public App addMenuMeta(String parentCode, SimpleMenu... menus) {
        return this.addMenuMeta(parentCode, Arrays.asList(menus));
    }

    public App addMenuMeta(String parentCode, List<SimpleMenu> menus) {
        if (this.menuHelper == null) {
            this.menuHelper = AppMenuHelper.of(this.appId);
        }
        if (parentCode == null || parentCode.trim().isEmpty()) {
            parentCode = ROOT_MENU_CODE;
        }
        this.menuHelper.addMeta(parentCode, menus);
        return this;
    }

    /**
     * 删除菜单
     *
     * @param menuCodes 菜单编码
     */
    public App removeMenuMeta(String... menuCodes) {
        return this.removeMenuMeta(Arrays.asList(menuCodes));
    }

    public App removeMenuMeta(List<String> menuCodes) {
        if (this.menuHelper == null) {
            this.menuHelper = AppMenuHelper.of(this.appId);
        }
        this.menuHelper.removeCodes(menuCodes);
        return this;
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

        if (this.appAdmins != null) {
            this.appAdmins.save();
        }
        if (this.menuHelper != null) {
            this.menuHelper.save();
        }
    }

    private App() {
        this.appId = AppId.of();
    }

    private App(Long appId) {
        this.appId = AppId.of(appId);
    }

}
