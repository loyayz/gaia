package com.loyayz.uaa.domain.role;

import com.loyayz.zeus.Identity;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class RoleId extends Identity<String> {

    static RoleId of(String code) {
        return new RoleId(code);
    }

    private RoleId(String id) {
        super(id);
    }

}
