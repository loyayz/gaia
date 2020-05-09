package com.loyayz.gaia.auth.core.authorization;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class AuthPermission implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 资源权限
     */
    private List<AuthResourcePermission> resources;
    /**
     * 角色权限
     * 角色只能访问的资源
     */
    private Map<String, List<AuthResource>> roles;

    public Map<AuthResource, List<String>> getResourceRoles() {
        if (this.resources == null) {
            return Collections.emptyMap();
        }
        return this.resources.stream()
                .filter(p -> p.getResource() != null && p.hasRoles())
                .collect(Collectors.toMap(
                        AuthResourcePermission::getResource,
                        AuthResourcePermission::getAllowedRoles,
                        (o1, o2) -> {
                            Set<String> r = new HashSet<>();
                            r.addAll(o1);
                            r.addAll(o2);
                            return Lists.newArrayList(r);
                        }
                ));
    }

    public Map<String, List<AuthResource>> getRoleResources() {
        if (this.roles == null) {
            return Collections.emptyMap();
        }
        return this.roles;
    }

    @Data
    @NoArgsConstructor
    private static class AuthResourcePermission {
        private AuthResource resource;
        private List<String> allowedRoles;

        public void setAllowedRoles(String allowedRoles) {
            List<String> others = null;
            if (allowedRoles != null) {
                others = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(allowedRoles);
            }
            this.setAllowedRoles(others);
        }

        public void setAllowedRoles(List<String> allowedRoles) {
            if (allowedRoles == null) {
                this.allowedRoles = Collections.emptyList();
            } else {
                this.allowedRoles = Lists.newArrayList(Sets.newHashSet(allowedRoles));
            }
        }

        public boolean hasRoles() {
            return this.allowedRoles != null && !this.allowedRoles.isEmpty();
        }

    }

}
