package com.loyayz.gaia.auth.core.authorization;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class DefaultAuthPermissionProvider implements AuthPermissionProvider {
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
     * 不需要鉴权的资源
     */
    private List<AuthResource> permit;
    /**
     * 资源权限
     */
    private AuthPermission permission;

    /**
     * 获取公开的资源列表
     */
    @Override
    public List<AuthResource> listPermitResources() {
        List<AuthResource> resources = new ArrayList<>();
        resources.addAll(this.getPermit());
        resources.addAll(this.getDefaultPermitResources());
        return resources;
    }

    @Override
    public AuthPermission getPermission() {
        if (this.permission == null) {
            return new AuthPermission();
        }
        return this.permission;
    }

    private List<AuthResource> getPermit() {
        if (this.permit == null) {
            return Collections.emptyList();
        }
        return permit;
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
