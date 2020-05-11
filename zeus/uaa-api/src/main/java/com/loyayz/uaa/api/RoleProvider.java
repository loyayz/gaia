package com.loyayz.uaa.api;

import com.loyayz.uaa.dto.RoleQueryParam;
import com.loyayz.uaa.dto.SimpleRole;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface RoleProvider {

    /**
     * 获取角色信息
     *
     * @param code 角色编码
     */
    SimpleRole getRole(String code);

    /**
     * 查询角色信息列表
     *
     * @param queryParam  查询条件
     */
    List<SimpleRole> listRole(RoleQueryParam queryParam);

}
