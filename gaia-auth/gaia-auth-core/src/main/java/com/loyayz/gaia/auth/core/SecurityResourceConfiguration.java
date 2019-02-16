package com.loyayz.gaia.auth.core;

import com.loyayz.gaia.auth.core.resource.SecurityResource;
import com.loyayz.gaia.auth.core.resource.SecurityResourcePermission;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SecurityResourceConfiguration {

    /**
     * 定义资源
     * < resourceGroup,resources >
     */
    private Map<String, List<SecurityResource>> items;
    /**
     * 权限配置
     */
    private SecurityResourcePermissionConfiguration permission;

    /**
     * @param resourceGroup group of resources
     * @return SecurityResource
     */
    public List<SecurityResource> listResourcesByGroup(String resourceGroup) {
        Collection<SecurityResource> resources = this.getItems()
                .getOrDefault(resourceGroup, Collections.emptyList())
                .stream()
                // 过滤掉空 path
                .filter(resource -> {
                    String path = resource.getPath();
                    return path != null && !"".equals(path.trim());
                })
                // 转为 map<path,resource>
                .collect(Collectors.toMap(
                        SecurityResource::getPath,
                        SecurityResource::new,
                        SecurityResource::combine
                ))
                .values();
        return new ArrayList<>(resources);
    }

    /**
     * 获取公开的资源列表
     */
    public List<SecurityResource> listPermitResources() {
        SecurityResourcePermissionConfiguration permissionConfig = this.getPermission();
        return permissionConfig.listPermitResources();
    }

    /**
     * 获取受保护资源的权限设置
     */
    public Map<SecurityResource, SecurityResourcePermission> listProtectResourcePermission() {
        Map<SecurityResource, SecurityResourcePermission> result = new HashMap<>(12);
        this.getPermission().getProtect().forEach((resourceGroup, permission) -> {
            this.listResourcesByGroup(resourceGroup).forEach(resource -> {
                result.put(resource, permission);
            });
        });
        return result;
    }

    public void setItems(Map<String, List<SecurityResource>> items) {
        if (items == null) {
            items = new HashMap<>(4);
        }
        this.items = items;
    }

    public void setPermission(SecurityResourcePermissionConfiguration permission) {
        if (permission == null) {
            permission = new SecurityResourcePermissionConfiguration();
        }
        this.permission = permission;
    }

}
