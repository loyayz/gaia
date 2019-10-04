package com.loyayz.gaia.auth.core.resource;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
public class AuthResourcePermission extends AuthResource {

    /**
     * 特殊字符，表示全部
     *
     * @see #allowedRoles
     */
    public static final String ALL = "*";

    private List<String> allowedRoles;

    public AuthResourcePermission(AuthResourcePermission other) {
        super(other);
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
    public AuthResourcePermission combine(AuthResourcePermission other) {
        if (other == null) {
            return this;
        }
        AuthResourcePermission result = new AuthResourcePermission(this);
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

    private List<String> combine(List<String> source, List<String> other) {
        if (other == null) {
            return (source != null ? source : Collections.emptyList());
        }
        if (source == null) {
            return other;
        }
        if (source.contains(ALL) || other.contains(ALL)) {
            return Lists.newArrayList(ALL);
        }
        Set<String> combined = new LinkedHashSet<>(source);
        combined.addAll(other);
        return new ArrayList<>(combined);
    }

}
