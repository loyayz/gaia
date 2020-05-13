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
public class UaaApp extends AbstractEntity<UaaApp> {

    /**
     * 名称
     */
    private String name;
    /**
     * 远程组件
     */
    private Integer remote;
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

}
