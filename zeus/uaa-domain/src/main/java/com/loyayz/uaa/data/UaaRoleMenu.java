package com.loyayz.uaa.data;

import com.loyayz.gaia.data.mybatis.AbstractTable;
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
public class UaaRoleMenu extends AbstractTable<UaaRoleMenu> {

    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 菜单
     */
    private Long menuId;


}
