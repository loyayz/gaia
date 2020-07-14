package com.loyayz.uaa.api;

import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import com.loyayz.uaa.dto.RoleQueryParam;
import com.loyayz.uaa.dto.SimpleRole;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface RoleQuery {

    /**
     * 根据id查询角色
     */
    SimpleRole getRole(Long roleId);

    /**
     * 分页查询角色
     */
    PageModel<SimpleRole> pageRole(RoleQueryParam queryParam, PageRequest pageRequest);

}
