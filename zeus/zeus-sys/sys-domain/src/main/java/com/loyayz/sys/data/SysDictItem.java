package com.loyayz.sys.data;

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
public class SysDictItem extends AbstractTable<SysDictItem> {

    /**
     * 字典编码
     */
    private String dictCode;
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
