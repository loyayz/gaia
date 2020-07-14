package com.loyayz.uaa.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleApp implements Serializable, Comparable<SimpleApp> {
    private static final long serialVersionUID = -1L;

    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 地址
     */
    private String url;
    /**
     * 备注
     */
    private String remark;
    /**
     * 序号
     */
    private Integer sort;
    /**
     * 创建日期
     */
    private Long createTime;

    /**
     * 根据 sort 降序
     */
    @Override
    public int compareTo(SimpleApp o) {
        if (this.sort == null) {
            return -1;
        }
        Integer otherSort = o.getSort();
        if (otherSort == null) {
            return 1;
        }
        if (this.sort.equals(otherSort)) {
            return 0;
        }
        return this.sort > otherSort ? -1 : 1;
    }

}
