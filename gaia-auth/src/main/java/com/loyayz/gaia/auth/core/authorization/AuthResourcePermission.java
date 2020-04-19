package com.loyayz.gaia.auth.core.authorization;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
public class AuthResourcePermission extends AuthResource {

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

}
