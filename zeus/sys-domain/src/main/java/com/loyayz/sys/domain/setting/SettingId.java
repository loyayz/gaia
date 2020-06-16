package com.loyayz.sys.domain.setting;

import com.loyayz.zeus.Identity;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class SettingId extends Identity<String> {

    static SettingId of(String id) {
        return new SettingId(id);
    }

    private SettingId(String id) {
        super(id);
    }

}
