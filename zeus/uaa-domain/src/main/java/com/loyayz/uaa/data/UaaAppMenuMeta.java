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
public class UaaAppMenuMeta extends AbstractTable<UaaAppMenuMeta> {

    /**
     * 应用
     */
    private Long appId;
    /**
     * 上级菜单
     */
    private Long pid;
    /**
     * 是否目录
     */
    private Integer dir;
    /**
     * 编码
     */
    private String code;
    /**
     * 菜单名
     */
    private String name;
    /**
     * 链接
     */
    private String url;
    /**
     * 图标
     */
    private String icon;
    /**
     * 序号a
     */
    private Integer sort;

}
