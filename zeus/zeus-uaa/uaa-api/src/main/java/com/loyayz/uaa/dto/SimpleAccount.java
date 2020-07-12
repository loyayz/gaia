package com.loyayz.uaa.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleAccount implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 账号类型
     */
    private String type;
    /**
     * 账号名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建日期
     */
    private Long createTime;

    public SimpleAccount() {

    }

    public SimpleAccount(SimpleAccount account, String password) {
        this.type = account.getType();
        this.name = account.getName();
        this.password = password;
        this.createTime = account.getCreateTime();
    }

    public void erasePassword() {
        this.password = null;
    }

}
