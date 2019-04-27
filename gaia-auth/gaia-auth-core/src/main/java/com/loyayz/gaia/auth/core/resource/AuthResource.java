package com.loyayz.gaia.auth.core.resource;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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

    public AuthResource getResource() {
        return new AuthResource(this);
    }

    public boolean valid() {
        return this.path != null
                && !"".equals(this.path.trim());
    }

}
