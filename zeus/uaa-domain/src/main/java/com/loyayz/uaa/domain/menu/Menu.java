package com.loyayz.uaa.domain.menu;

import com.loyayz.uaa.data.UaaMenu;
import com.loyayz.uaa.domain.MenuRepository;
import com.loyayz.zeus.AbstractEntity;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Menu extends AbstractEntity<UaaMenu> {
    private final MenuId menuId;

    public static Menu of() {
        return new Menu();
    }

    public static Menu of(Long menuId) {
        return new Menu(menuId);
    }

    public Long id() {
        return this.menuId.get();
    }

    public Menu appId(Long appId) {
        super.entity().setAppId(appId);
        super.markUpdated();
        return this;
    }

    public Menu pid(Long pid) {
        UaaMenu entity = super.entity();
        entity.setPid(pid);
        if (entity.getSort() == null) {
            this.sort(MenuRepository.getNextSort(pid));
        }
        super.markUpdated();
        return this;
    }

    public Menu metaId(Long metaId) {
        super.entity().setMenuMetaId(metaId);
        super.markUpdated();
        return this;
    }

    public Menu name(String name) {
        super.entity().setName(name);
        super.markUpdated();
        return this;
    }

    public Menu icon(String icon) {
        super.entity().setIcon(icon);
        super.markUpdated();
        return this;
    }

    public Menu sort(int sort) {
        super.entity().setSort(sort);
        super.markUpdated();
        return this;
    }

    /**
     * 隐藏菜单
     */
    public Menu hidden() {
        super.entity().setHidden(1);
        super.markUpdated();
        return this;
    }

    /**
     * 开放菜单
     */
    public Menu open() {
        super.entity().setHidden(0);
        super.markUpdated();
        return this;
    }

    @Override
    protected UaaMenu buildEntity() {
        if (this.menuId.isEmpty()) {
            UaaMenu entity = new UaaMenu();
            entity.setName("");
            entity.setIcon("");
            entity.setHidden(0);
            return entity;
        } else {
            return MenuRepository.findById(this.id());
        }
    }

    @Override
    public void save() {
        super.save();
        this.menuId.set(super.entity().getId());
    }

    private Menu() {
        this.menuId = MenuId.of();
    }

    private Menu(Long appId) {
        this.menuId = MenuId.of(appId);
    }

}
