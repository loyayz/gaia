package com.loyayz.gaia.auth.core;

import com.loyayz.gaia.auth.core.resource.SecurityResource;
import com.loyayz.gaia.auth.core.resource.SecurityResourcePermission;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SecurityResourcePermissionConfiguration implements Serializable {
    private static final long serialVersionUID = -1L;

    private static final List<String> PERMIT_DEFAULT_PUBLIC = Arrays.asList("/public/**", "/error/**");
    private static final List<String> PERMIT_DEFAULT_STATIC = Arrays.asList("/css/**", "/js/**", "/images/**", "/webjars/**", "/**/favicon.ico", "/static/**");

    /**
     * 不鉴权所有公共资源
     *
     * @see #getDefaultPermitResources
     */
    private Boolean permitPublic = Boolean.TRUE;
    /**
     * 不鉴权所有静态资源
     *
     * @see #getDefaultPermitResources
     */
    private Boolean permitStatic = Boolean.TRUE;
    /**
     * 不鉴权所有 options 请求
     *
     * @see #getDefaultPermitResources
     */
    private Boolean permitOptions = Boolean.TRUE;

    /**
     * 不需要鉴权的资源组（resourceGroup）
     */
    private List<SecurityResource> permit;
    /**
     * 受保护的资源 （需要鉴权）
     * < resourceGroup,permission >
     */
    private Map<String, SecurityResourcePermission> protect;

    public List<SecurityResource> listPermitResources() {
        List<SecurityResource> resources = new ArrayList<>();
        resources.addAll(this.getPermit());
        resources.addAll(this.getDefaultPermitResources());
        return resources;
    }

    /**
     * 获取不需要鉴权的资源
     */
    private List<SecurityResource> getDefaultPermitResources() {
        List<SecurityResource> result = new ArrayList<>();
        if (this.permitPublic) {
            for (String path : PERMIT_DEFAULT_PUBLIC) {
                result.add(new SecurityResource(path));
            }
        }
        if (this.permitStatic) {
            for (String path : PERMIT_DEFAULT_STATIC) {
                result.add(new SecurityResource(path));
            }
        }
        if (this.permitOptions) {
            SecurityResource options = new SecurityResource("/**");
            options.setMethods("OPTIONS");
            result.add(options);
        }
        return result;
    }

    public List<SecurityResource> getPermit() {
        if (this.permit == null) {
            return Collections.emptyList();
        }
        return this.permit;
    }

    public Map<String, SecurityResourcePermission> getProtect() {
        if (this.protect == null) {
            return Collections.emptyMap();
        }
        return this.protect;
    }

}
