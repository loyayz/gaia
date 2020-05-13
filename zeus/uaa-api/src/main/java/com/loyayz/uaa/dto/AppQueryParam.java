package com.loyayz.uaa.dto;

import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class AppQueryParam  {

    /**
     * 名称
     * 模糊查询
     */
    private String name;
    /**
     * 远程组件
     */
    private Integer remote;

}
