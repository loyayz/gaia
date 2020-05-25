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
public class UaaRoleApp extends AbstractTable<UaaRoleApp> {

    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 应用
     */
    private Long appId;


}
