package com.loyayz.sys.common.dto;

import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleDictItem {

    /**
     * 数据名
     */
    private String name;
    /**
     * 数据值
     */
    private String value;
    /**
     * 序号
     */
    private Integer sort;

}
