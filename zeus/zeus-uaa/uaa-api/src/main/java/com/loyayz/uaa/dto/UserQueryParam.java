package com.loyayz.uaa.dto;

import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class UserQueryParam {

    /**
     * 用户名
     * 模糊查询
     */
    private String name;
    /**
     * 电话
     * 模糊查询
     */
    private String mobile;
    /**
     * 邮箱
     * 模糊查询
     */
    private String email;
    /**
     * 是否锁定
     */
    private Integer locked;
    /**
     * 角色
     */
    private Long roleId;

}
