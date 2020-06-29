package com.loyayz.uaa.query;

import com.loyayz.uaa.data.UaaApp;
import com.loyayz.uaa.data.converter.AppConverter;
import com.loyayz.uaa.domain.AppRepository;
import com.loyayz.uaa.dto.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.loyayz.uaa.constant.UaaConstant.ROOT_MENU_CODE;

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
        // pid,menu
        Map<Long, List<SimpleMenu>> parentMenus = new HashMap<>(16);
        listMenu(queryParam).forEach(menu -> {
            Long pid = menu.getPid();
            if (ROOT_MENU_CODE.equals(pid)) {
                result.add(menu);
            } else {
                List<SimpleMenu> tempMenus = parentMenus.getOrDefault(pid, new ArrayList<>());
                tempMenus.add(menu);
                parentMenus.put(pid, tempMenus);
            }
        });
        for (SimpleMenu dir : result) {
            addMenu(dir, parentMenus);
        }
        return result;
    }

    public static List<SimpleMenuAction> listMenuAction(Long menuId) {
        return AppRepository.listAppMenuActionByMenu(menuId)
                .stream()
                .map(AppConverter::toSimple)
                .collect(Collectors.toList());
    }

    private static void addMenu(SimpleMenu dir, Map<Long, List<SimpleMenu>> parentMenus) {
        if (!dir.getDir()) {
            return;
        }
        List<SimpleMenu> subMenus = parentMenus.getOrDefault(dir.getId(), Collections.emptyList());
        for (SimpleMenu subMenu : subMenus) {
            addMenu(subMenu, parentMenus);

            dir.addItem(subMenu);
        }
    }

}
