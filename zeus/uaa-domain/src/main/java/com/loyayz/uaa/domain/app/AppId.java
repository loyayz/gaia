package com.loyayz.uaa.domain.app;

import com.loyayz.zeus.Identity;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class AppId extends Identity<Long> {

    static AppId of() {
        return of(null);
    }

    static AppId of(Long id) {
        return new AppId(id);
    }

    private AppId(Long id) {
        super(id);
    }

}
