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
public class UaaMenu extends AbstractTable<UaaMenu> {

    /**
     * 应用
     */
    private Long appId;
    /**
     * 上级id
     */
    private Long pid;
    /**
     * 菜单元数据
     */
    private Long menuMetaId;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 是否隐藏
     */
    private Integer hidden;
    /**
     * 序号
     */
    private Integer sort;

}
