package com.loyayz.uaa.domain.command;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.api.App;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.data.entity.UaaApp;
import com.loyayz.uaa.data.entity.UaaAppMenu;
import com.loyayz.uaa.data.entity.UaaAppMenuAction;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.uaa.dto.MenuDirectory;
import com.loyayz.uaa.dto.SimpleApp;
import com.loyayz.uaa.dto.SimpleMenu;
import com.loyayz.uaa.dto.SimpleMenuAction;
import com.loyayz.uaa.exception.ExistChildMenuException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.loyayz.uaa.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class AppCommand implements App {
    private final Long appId;

    private AppCommand(Long appId) {
        this.appId = appId;
    }

    public static App getInstance(Long appId) {
        return new AppCommand(appId);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuMapper#deleteByApp}
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByApp}
     */
    @Override
    public void delete() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("appId", this.appId);

        new UaaApp().deleteById(this.appId);
        MybatisUtils.executeDelete(UaaAppMenu.class, "deleteByApp", param);
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByApp", param);
    }

    @Override
    public void update(SimpleApp app) {
        UaaApp entity = new UaaApp().findById(this.appId);
        AppConverter.setEntity(entity, app);
        entity.updateById();
    }

    @Override
    public MenuDirectory addOrUpdateMenu(MenuDirectory dir) {
        if (dir.getCode() == null) {
            dir.setCode(ROOT_MENU_CODE);
        }
        this.saveMenuDirectory(dir);
        return dir;
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuMapper#deleteByCode}
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByMenu}
     */
    @Override
    public void deleteMenu(String menuCode) {
        Integer subMenus = AppRepository.countAppMenuByParent(menuCode);
        if (subMenus > 0) {
            throw new ExistChildMenuException(menuCode);
        }

        Map<String, Object> param = new HashMap<>(3);
        param.put("appId", this.appId);
        param.put("menuCode", menuCode);
        MybatisUtils.executeDelete(UaaAppMenu.class, "deleteByCode", param);
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByMenu", param);
    }

    @Override
    public void addMenuAction(String menuCode, SimpleMenuAction action) {
        action.setMenuCode(menuCode);
        UaaAppMenuAction entity = AppConverter.toEntity(this.appId, action);
        entity.insert();
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaAppMenuActionMapper#deleteByCode}
     */
    @Override
    public void deleteMenuAction(String menuCode, List<String> actionCodes) {
        if (actionCodes.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("appId", this.appId);
        param.put("menuCode", menuCode);
        param.put("actionCodes", actionCodes);
        MybatisUtils.executeDelete(UaaAppMenuAction.class, "deleteByCode", param);
    }

    private void saveMenuDirectory(MenuDirectory dir) {
        this.addOrUpdateMenuDirectory(dir);

        String dirCode = dir.getCode();
        for (MenuDirectory subDir : dir.getDirs()) {
            this.saveMenuDirectory(subDir);
        }
        for (SimpleMenu menu : dir.getMenus()) {
            menu.setParentCode(dirCode);
            this.addOrUpdateMenu(menu);
        }
    }

    private void addOrUpdateMenuDirectory(MenuDirectory dir) {
        if (ROOT_MENU_CODE.equals(dir.getCode())) {
            return;
        }
        UaaAppMenu entity = AppRepository.getAppMenuByCode(dir.getCode());
        if (entity == null) {
            if (dir.getParentCode() == null) {
                dir.setParentCode(ROOT_MENU_CODE);
            }
            entity = AppConverter.toEntity(this.appId, dir);
            entity.insert();
        } else {
            AppConverter.setEntity(entity, this.appId, dir);
            entity.updateById();
        }
    }

    private void addOrUpdateMenu(SimpleMenu menu) {
        UaaAppMenu entity = AppRepository.getAppMenuByCode(menu.getCode());
        if (entity == null) {
            entity = AppConverter.toEntity(this.appId, menu);
            entity.insert();
        } else {
            AppConverter.setEntity(entity, this.appId, menu);
            entity.updateById();
        }
    }

}
