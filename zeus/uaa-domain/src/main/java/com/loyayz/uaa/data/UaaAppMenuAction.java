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
     * 菜单
     */
    private Long menuMetaId;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;

}
