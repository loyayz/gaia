package com.loyayz.sys.dto;

import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class SimpleDict {

    /**
     * 分组名
     */
    private String groupName;
    /**
     * 字典编码
     */
    private String dictCode;
    /**
     * 字典名
     */
    private String dictName;
    /**
     * 序号
     */
    private Integer sort;

}
