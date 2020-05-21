package com.loyayz.uaa.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleMenu implements Serializable {
    private static final long serialVersionUID = -1L;

    private String parentCode;
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
     * 序号
     */
    private Integer sort;

    /**
     * 子菜单
     */
    private List<SimpleMenu> items = new ArrayList<>();
    /**
     * 是否目录
     */
    private Boolean dir;

    public Integer getSort() {
        return sort == null ? 0 : sort;
    }

    public List<SimpleMenu> getItems() {
        return items == null ? new ArrayList<>() : items;
    }

    /**
     * 有子菜单时，肯定是目录
     * 否则默认非目录
     */
    public Boolean getDir() {
        if (!this.items.isEmpty()) {
            return true;
        }
        return dir == null ? false : dir;
    }

    public SimpleMenu addItem(SimpleMenu menu) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(menu);
        return this;
    }

}
