package com.loyayz.uaa.query;

import com.loyayz.uaa.common.dto.*;
import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;

import java.util.*;
import java.util.stream.Collectors;

import static com.loyayz.uaa.common.constant.UaaConstant.ROOT_MENU_CODE;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class AppQuery {

    public static SimpleApp getApp(Long appId) {
        return Optional.ofNullable(new UaaApp().findById(appId))
                .map(AppConverter::toSimple)
                .orElse(null);
    }

    public static List<SimpleApp> listApp(AppQueryParam queryParam) {
        return AppRepository.listAppByParam(queryParam)
                .stream()
                .map(AppConverter::toSimple)
                .collect(Collectors.toList());
    }

    public static List<SimpleMenu> listMenu(MenuQueryParam queryParam) {
        return AppRepository.listAppMenuByParam(queryParam)
                .stream()
                .map(AppConverter::toSimple)
                .collect(Collectors.toList());
    }

    public static List<SimpleMenu> listMenuTree(Long appId) {
        MenuQueryParam queryParam = new MenuQueryParam();
        queryParam.setAppId(appId);

        List<SimpleMenu> result = new ArrayList<>();
        // parentCode,menu
        Map<String, List<SimpleMenu>> parentMenus = new HashMap<>(16);
        listMenu(queryParam).forEach(menu -> {
            String parentCode = menu.getParentCode();
            if (ROOT_MENU_CODE.equals(parentCode)) {
                result.add(menu);
            } else {
                List<SimpleMenu> tempMenus = parentMenus.getOrDefault(parentCode, new ArrayList<>());
                tempMenus.add(menu);
                parentMenus.put(parentCode, tempMenus);
            }
        });
        for (SimpleMenu dir : result) {
            addMenu(dir, parentMenus);
        }
        return result;
    }

    public static List<SimpleMenuAction> listAppAction(Long appId) {
        return AppRepository.listAppMenuActionByApp(appId)
                .stream()
                .map(AppConverter::toSimple)
                .collect(Collectors.toList());
    }

    public static List<SimpleMenuAction> listMenuAction(String menuCode) {
        return AppRepository.listAppMenuActionByMenu(menuCode)
                .stream()
                .map(AppConverter::toSimple)
                .collect(Collectors.toList());
    }

    private static void addMenu(SimpleMenu dir, Map<String, List<SimpleMenu>> parentMenus) {
        if (!dir.getDir()) {
            return;
        }
        List<SimpleMenu> subMenus = parentMenus.getOrDefault(dir.getCode(), Collections.emptyList());
        for (SimpleMenu subMenu : subMenus) {
            addMenu(subMenu, parentMenus);

            dir.addItem(subMenu);
        }
    }

}
