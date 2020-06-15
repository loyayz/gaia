package com.loyayz.uaa.domain.dept;

import com.loyayz.zeus.Identity;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class DeptId extends Identity<Long> {

    static DeptId of(Long id) {
        return new DeptId(id);
    }

    private DeptId(Long id) {
        super(id);
    }

}
