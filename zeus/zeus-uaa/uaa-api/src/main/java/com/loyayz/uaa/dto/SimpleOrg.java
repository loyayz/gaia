package com.loyayz.uaa.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleOrg implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 序号
     */
    private Integer sort;
    /**
     * 创建日期
     */
    private Long createTime;
    /**
     * 下级组织
     */
    private List<SimpleOrg> items = new ArrayList<>();

}
