package com.loyayz.uaa.domain.user;

import com.loyayz.zeus.Identity;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class UserId extends Identity<Long> {

    static UserId of() {
        return of(null);
    }

    static UserId of(Long id) {
        return new UserId(id);
    }

    private UserId(Long id) {
        super(id);
    }

}
