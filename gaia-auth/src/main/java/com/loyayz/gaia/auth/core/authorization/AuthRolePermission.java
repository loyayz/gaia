package com.loyayz.gaia.auth.core.authorization;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
public class AuthRolePermission implements Serializable {
    private static final long serialVersionUID = -1L;

    private String role;
    private List<AuthResource> resources;

    public boolean valid() {
        return role != null && resources == null;
    }

}
