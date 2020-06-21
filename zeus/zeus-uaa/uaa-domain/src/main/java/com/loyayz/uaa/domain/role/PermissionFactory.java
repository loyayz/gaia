package com.loyayz.uaa.domain.role;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class PermissionFactory {

    public static BasePermission menu(Long menuId) {
        BasePermission permission = new Menu();
        permission.refId(menuId);
        return permission;
    }

    public static BasePermission menuAction(Long actionId) {
        BasePermission permission = new MenuAction();
        permission.refId(actionId);
        return permission;
    }

    private static class Menu extends BasePermission {
        @Override
        public String type() {
            return "menu";
        }
    }

    private static class MenuAction extends BasePermission {
        @Override
        public String type() {
            return "menu_action";
        }
    }

}
