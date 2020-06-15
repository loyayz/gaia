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
public class UaaDept extends AbstractTable<UaaDept> {

    /**
     * 上级部门
     */
    private Long pid;
    /**
     * 名称
     */
    private String name;
    /**
     * 序号
     */
    private Integer sort;

}
