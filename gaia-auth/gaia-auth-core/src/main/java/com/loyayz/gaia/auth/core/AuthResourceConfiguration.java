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
    private static final List<String> DEFAULT_PERMIT_PUBLIC = Arrays.asList("/public/**", "/error/**");
    private static final List<String> DEFAULT_PERMIT_STATIC = Arrays.asList("/css/**", "/js/**", "/images/**", "/webjars/**", "/**/favicon.ico", "/static/**");

    /**
     * 不鉴权所有公共资源
     */
    private Boolean permitPublic = Boolean.TRUE;
    /**
     * 不鉴权所有静态资源
     */
    private Boolean permitStatic = Boolean.TRUE;
    /**
     * 不鉴权所有 options 请求
     */
    private Boolean permitOptions = Boolean.TRUE;
    /**
     * 不需要鉴权的资源组（resourceGroup）
     */
    private List<AuthResource> permit;

    /**
     * 权限配置
     */
    private List<AuthResourcePermission> permission;

    /**
     * 获取受保护资源的权限设置
     */
    public Map<AuthResource, AuthResourcePermission> listProtectResources() {
        return this.getPermission()
                .stream()
                // 过滤掉不合法的配置
                .filter(p -> p != null && p.valid())
                // 转为 map<resource,permission>
                .collect(Collectors.toMap(
                        AuthResourcePermission::getResource,
                        AuthResourcePermission::new,
                        AuthResourcePermission::combine)
                );
    }

    /**
     * 获取公开的资源列表
     */
    public List<AuthResource> listPermitResources() {
        List<AuthResource> resources = new ArrayList<>();
        resources.addAll(this.getPermit());
        resources.addAll(this.getDefaultPermitResources());
        return resources;
    }

    public List<AuthResource> getPermit() {
        if (this.permit == null) {
            return Collections.emptyList();
        }
        return permit;
    }

    public List<AuthResourcePermission> getPermission() {
        if (this.permission == null) {
            return Collections.emptyList();
        }
        return permission;
    }

    /**
     * 获取不需要鉴权的资源
     */
    private List<AuthResource> getDefaultPermitResources() {
        List<AuthResource> result = new ArrayList<>();
        if (this.permitPublic) {
            for (String path : DEFAULT_PERMIT_PUBLIC) {
                result.add(new AuthResource(path));
            }
        }
        if (this.permitStatic) {
            for (String path : DEFAULT_PERMIT_STATIC) {
                result.add(new AuthResource(path));
            }
        }
        if (this.permitOptions) {
            AuthResource options = new AuthResource("/**");
            options.setMethods("OPTIONS");
            result.add(options);
        }
        return result;
    }

}
