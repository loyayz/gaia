package com.loyayz.gaia.auth.core.authorization;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthResourceService {

    /**
     * 获取公开的资源列表
     */
    List<AuthResource> listPermitResources();

    /**
     * 获取受保护资源的权限设置
     */
    List<AuthResourcePermission> listResourcePermissions();

    /**
     * 获取角色只能访问的资源
     */
    List<AuthRolePermission> listRolePermissions();

}
