package com.loyayz.gaia.auth.core;

import com.loyayz.gaia.auth.core.resource.AuthResource;
import com.loyayz.gaia.auth.core.resource.AuthResourcePermission;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class AuthResourceConfiguration {

    /**
     * 定义资源
     * < resourceGroup,resources >
     */
    private Map<String, List<AuthResource>> items;
    /**
     * 权限配置
     */
    private AuthResourcePermissionConfiguration permission;

    /**
     * @param resourceGroup group of resources
     * @return resources
     */
    public List<AuthResource> listResourcesByGroup(String resourceGroup) {
        Collection<AuthResource> resources = this.getItems()
                .getOrDefault(resourceGroup, Collections.emptyList())
                .stream()
                // 过滤掉空 path
                .filter(resource -> {
                    String path = resource.getPath();
                    return path != null && !"".equals(path.trim());
                })
                // 转为 map<path,resource>
                .collect(Collectors.toMap(
                        AuthResource::getPath,
                        AuthResource::new,
                        AuthResource::combine
                ))
                .values();
        return new ArrayList<>(resources);
    }

    /**
     * 获取公开的资源列表
     */
    public List<AuthResource> listPermitResources() {
        AuthResourcePermissionConfiguration permissionConfig = this.getPermission();
        return permissionConfig.listPermitResources();
    }

    /**
     * 获取受保护资源的权限设置
     */
    public Map<AuthResource, AuthResourcePermission> listProtectResourcePermission() {
        Map<AuthResource, AuthResourcePermission> result = new HashMap<>(12);
        this.getPermission().getProtect().forEach((resourceGroup, permission) -> {
            this.listResourcesByGroup(resourceGroup).forEach(resource -> {
                result.put(resource, permission);
            });
        });
        return result;
    }

    public void setItems(Map<String, List<AuthResource>> items) {
        if (items == null) {
            items = new HashMap<>(4);
        }
        this.items = items;
    }

    public void setPermission(AuthResourcePermissionConfiguration permission) {
        if (permission == null) {
            permission = new AuthResourcePermissionConfiguration();
        }
        this.permission = permission;
    }

}
