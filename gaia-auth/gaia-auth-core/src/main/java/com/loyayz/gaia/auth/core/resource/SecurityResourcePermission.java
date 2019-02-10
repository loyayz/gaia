package com.loyayz.gaia.auth.core.resource;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
public class SecurityResourcePermission implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 特殊字符，表示全部
     *
     * @see #allowedRoles
     */
    public static final String ALL = "*";

    private List<String> allowedRoles;

    public SecurityResourcePermission(SecurityResourcePermission other) {
        this.allowedRoles = other.getAllowedRoles();
    }

    /**
     * 是否不需要鉴权
     */
    public boolean isPermit() {
        return this.getAllowedRoles().contains(ALL);
    }

    /**
     * 合并权限
     * allowedRoles 包含 {@link #ALL} 则合并后还是为 {@link #ALL}
     */
    public SecurityResourcePermission combine(SecurityResourcePermission other) {
        if (other == null) {
            return this;
        }
        SecurityResourcePermission result = new SecurityResourcePermission(this);
        result.setAllowedRoles(this.combine(this.getAllowedRoles(), other.getAllowedRoles()));
        return result;
    }

    public void setAllowedRoles(String allowedRoles) {
        List<String> others = null;
        if (allowedRoles != null) {
            others = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(allowedRoles);
        }
        this.setAllowedRoles(others);
    }

    public void setAllowedRoles(List<String> allowedRoles) {
        if (allowedRoles == null || allowedRoles.isEmpty() || allowedRoles.contains(ALL)) {
            this.allowedRoles = Lists.newArrayList(ALL);
        } else {
            this.allowedRoles = Lists.newArrayList(Sets.newHashSet(allowedRoles));
        }
    }

    public List<String> getAllowedRoles() {
        if (this.allowedRoles == null || this.allowedRoles.isEmpty() || this.allowedRoles.contains(ALL)) {
            return Lists.newArrayList(ALL);
        }
        return this.allowedRoles;
    }

    public void addAllowedRoles(String role) {
        if (ALL.equals(role)) {
            this.allowedRoles = Lists.newArrayList(ALL);
            return;
        }
        if (this.allowedRoles == null) {
            this.allowedRoles = Lists.newArrayList();
        }
        this.allowedRoles.add(role);
    }

    private List<String> combine(List<String> source, List<String> other) {
        if (other == null) {
            return (source != null ? source : Collections.emptyList());
        }
        if (source == null) {
            return other;
        }
        if (source.contains(ALL) || other.contains(ALL)) {
            return new ArrayList<>(Collections.singletonList(ALL));
        }
        Set<String> combined = new LinkedHashSet<>(source);
        combined.addAll(other);
        return new ArrayList<>(combined);
    }

}
