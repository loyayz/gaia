package com.loyayz.gaia.auth.core.resource;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
public class AuthResource implements Serializable {
    private static final long serialVersionUID = -1L;
    public static final String ALL_METHOD = "*";

    private String path;
    private List<String> methods;

    /**
     * 合并资源
     * methods 包含 {@link #ALL_METHOD} 则合并后还是为 {@link #ALL_METHOD}
     */
    public AuthResource combine(AuthResource other) {
        AuthResource result = new AuthResource(this);
        if (other == null) {
            return result;
        }
        result.setMethods(this.combine(this.getMethods(), other.getMethods()));
        return result;
    }

    public AuthResource(String path) {
        this.path = path;
    }

    public AuthResource(AuthResource other) {
        this.path = other.getPath();
        this.methods = other.getMethods();
    }

    public void setMethods(String methods) {
        List<String> others = null;
        if (methods != null) {
            others = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(methods);
        }
        this.setMethods(others);
    }

    public void setMethods(List<String> methods) {
        if (methods == null || methods.isEmpty() || methods.contains(ALL_METHOD)) {
            this.methods = Lists.newArrayList(ALL_METHOD);
        } else {
            this.methods = Lists.newArrayList(Sets.newHashSet(methods));
        }
    }

    public List<String> getMethods() {
        if (this.methods == null || this.methods.isEmpty() || this.methods.contains(ALL_METHOD)) {
            return Lists.newArrayList(ALL_METHOD);
        }
        return this.methods;
    }

    public void addMethod(String method) {
        if (ALL_METHOD.equals(method)) {
            this.methods = Lists.newArrayList(ALL_METHOD);
            return;
        }
        if (this.methods == null) {
            this.methods = Lists.newArrayList();
        }
        this.methods.add(method);
    }

    private List<String> combine(List<String> source, List<String> other) {
        if (other == null) {
            return (source != null ? source : Collections.emptyList());
        }
        if (source == null) {
            return other;
        }
        Set<String> combined = Sets.newLinkedHashSet(source);
        combined.addAll(other);
        return Lists.newArrayList(combined);
    }

}
