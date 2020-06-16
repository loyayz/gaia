package com.loyayz.sys.domain.dict;

import com.loyayz.zeus.Identity;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class DictId extends Identity<String> {

    static DictId of(String id) {
        return new DictId(id);
    }

    private DictId(String id) {
        super(id);
    }

}
