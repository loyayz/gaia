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
public class UaaOrgRole extends AbstractTable<UaaOrgRole> {

    /**
     * 组织
     */
    private Long orgId;
    /**
     * 角色
     */
    private Long roleId;

}
