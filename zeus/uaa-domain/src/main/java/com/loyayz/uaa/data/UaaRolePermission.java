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
public class UaaRolePermission extends AbstractTable<UaaRolePermission> {

    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 权限类型
     */
    private String type;
    /**
     * 关联对象
     */
    private Long refId;

}
