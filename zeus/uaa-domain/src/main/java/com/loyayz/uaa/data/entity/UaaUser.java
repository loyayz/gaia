package com.loyayz.uaa.data.entity;

import com.loyayz.gaia.data.mybatis.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UaaUser extends AbstractEntity<UaaUser> {

    /**
     * 用户名
     */
    private String name;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 详情
     */
    private String info;
    /**
     * 是否锁定
     */
    private Integer locked;
    /**
     * 是否删除
     */
    private Integer deleted;

}
