package com.loyayz.uaa.common.constant;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public enum RolePermissionType {
    /**
     * 权限类型：应用
     * uaa_app
     */
    APP("app"),
    /**
     * 权限类型：菜单
     * uaa_menu
     */
    MENU("menu"),
    /**
     * 权限类型：菜单功能
     * uaa_app_menu_action
     */
    ACTION("menu_action");

    private String val;

    RolePermissionType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

}
