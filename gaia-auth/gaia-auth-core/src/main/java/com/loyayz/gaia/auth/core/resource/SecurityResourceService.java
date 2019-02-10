package com.loyayz.gaia.auth.core.resource;

import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface SecurityResourceService {

    /**
     * 根据分组名查询资源列表
     */
    List<SecurityResource> listResourcesByGroup(String resourceGroup);

    /**
     * 获取公开的资源列表
     */
    List<SecurityResource> listPermitResources();

    /**
     * 获取受保护资源的权限设置
     */
    Map<SecurityResource, SecurityResourcePermission> listProtectResourcePermission();

}
