package com.loyayz.gaia.auth.core.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements Serializable {
    private static final long serialVersionUID = -1L;

    private String id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 角色
     */
    private List<AuthUserRole> roles = new ArrayList<>();

    public List<AuthUserRole> getRoles() {
        if (this.roles == null) {
            return Collections.emptyList();
        }
        return roles;
    }

}
