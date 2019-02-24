package com.loyayz.gaia.auth.core.resource;

import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface AuthResourceService {

    /**
     * 根据分组名查询资源列表
     */
    List<AuthResource> listResourcesByGroup(String resourceGroup);

    /**
     * 获取公开的资源列表
     */
    List<AuthResource> listPermitResources();

    /**
     * 获取受保护资源的权限设置
     */
    Map<AuthResource, AuthResourcePermission> listProtectResourcePermission();

}
