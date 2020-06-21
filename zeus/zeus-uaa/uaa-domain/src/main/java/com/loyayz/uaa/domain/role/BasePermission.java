package com.loyayz.uaa.domain.role;

import com.loyayz.zeus.ValueObject;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class BasePermission implements ValueObject {
    private Long refId;

    /**
     * 权限类型
     */
    public abstract String type();

    public void refId(Long refId) {
        this.refId = refId;
    }

    public Long refId() {
        return this.refId;
    }

}
