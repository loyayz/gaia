package com.loyayz.uaa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -1L;

    private SimpleUser user;
    /**
     * 账号
     */
    private List<SimpleAccount> accounts;
    /**
     * 角色
     */
    private List<SimpleRole> roles;

    public void eraseCredentials() {
        if (this.accounts != null) {
            for (SimpleAccount account : this.accounts) {
                account.erasePassword();
            }
        }
    }

}
