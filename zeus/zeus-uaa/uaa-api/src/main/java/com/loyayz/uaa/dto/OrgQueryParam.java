package com.loyayz.uaa.dto;

import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class OrgQueryParam {

    private Long pid;
    /**
     * 组织名
     * 模糊查询
     */
    private String name;

}
