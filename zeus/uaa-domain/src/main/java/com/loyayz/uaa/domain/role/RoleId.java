package com.loyayz.uaa.domain.role;

import com.loyayz.zeus.Identity;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class RoleId extends Identity<Long> {

    static RoleId of(Long id) {
        return new RoleId(id);
    }

    private RoleId(Long id) {
        super(id);
    }

}
