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
public class UaaAppMenuAction extends AbstractTable<UaaAppMenuAction> {

    /**
     * 应用
     */
    private Long appId;
    /**
     * 菜单
     */
    private String menuCode;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;

}
