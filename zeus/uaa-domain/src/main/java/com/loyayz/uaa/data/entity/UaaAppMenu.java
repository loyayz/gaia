package com.loyayz.uaa.data.entity;

import com.loyayz.gaia.data.mybatis.AbstractEntity;
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
public class UaaAppMenu extends AbstractEntity<UaaAppMenu> {

    /**
     * 应用
     */
    private Long appId;
    /**
     * 上级编码
     */
    private String parentCode;
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
     * 备注
     */
    private String remark;
    /**
     * 序号a
     */
    private Integer sort;

}
