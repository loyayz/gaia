package com.loyayz.uaa.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuDirectory extends SimpleMenu {

    /**
     * 菜单列表
     */
    private List<SimpleMenu> menus = new ArrayList<>();
    /**
     * 子目录列表
     */
    private List<MenuDirectory> dirs = new ArrayList<>();
    /**
     * 是否目录
     */
    private Boolean dir = true;

    public void addMenu(SimpleMenu menu) {
        if (menu instanceof MenuDirectory) {
            this.dirs.add((MenuDirectory) menu);
        } else {
            this.menus.add(menu);
        }
    }

}
