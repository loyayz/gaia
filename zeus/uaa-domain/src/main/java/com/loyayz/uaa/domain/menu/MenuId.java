package com.loyayz.uaa.domain.menu;

import com.loyayz.zeus.Identity;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class MenuId extends Identity<Long> {

    static MenuId of() {
        return of(null);
    }

    static MenuId of(Long id) {
        return new MenuId(id);
    }

    private MenuId(Long id) {
        super(id);
    }

}
