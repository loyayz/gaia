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
public class UaaDeptUser extends AbstractTable<UaaDeptUser> {

    /**
     * 部门
     */
    private Long deptId;
    /**
     * 用户
     */
    private Long userId;

}
