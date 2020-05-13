package com.loyayz.uaa.api;

import com.loyayz.uaa.dto.MenuDirectory;
import com.loyayz.uaa.dto.SimpleApp;
import com.loyayz.uaa.dto.SimpleMenuAction;

import java.util.Arrays;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface App {

    /**
     * 删除应用
     */
    void delete();

    /**
     * 修改应用信息
     */
    void update(SimpleApp app);

    /**
     * 新增或修改菜单信息
     *
     * @param dir 菜单目录
     * @return 菜单目录
     */
    MenuDirectory addOrUpdateMenu(MenuDirectory dir);

    /**
     * 删除菜单
     * 有下级菜单时无法删除
     *
     * @param menuCode 菜单编码
     */
    void deleteMenu(String menuCode);

    /**
     * 新增菜单功能
     *
     * @param menuCode 菜单编码
     * @param action   功能信息
     */
    void addMenuAction(String menuCode, SimpleMenuAction action);

    /**
     * 删除菜单功能
     *
     * @param menuCode    菜单编码
     * @param actionCodes 功能编码
     */
    default void deleteMenuAction(String menuCode, String... actionCodes) {
        this.deleteMenuAction(menuCode, Arrays.asList(actionCodes));
    }

    /**
     * 删除菜单功能
     *
     * @param menuCode    菜单编码
     * @param actionCodes 功能编码
     */
    void deleteMenuAction(String menuCode, List<String> actionCodes);

}
