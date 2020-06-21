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
public class UaaDeptRole extends AbstractTable<UaaDeptRole> {

    /**
     * 部门
     */
    private Long deptId;
    /**
     * 角色
     */
    private Long roleId;

}
