package com.loyayz.gaia.auth.core.authorization;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthPermissionProvider {

    /**
     * 获取公开的资源列表
     */
    List<AuthResource> listPermitResources();

    /**
     * 获取资源的权限设置
     */
    AuthPermission getPermission();

}
