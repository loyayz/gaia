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
public class SysDict extends AbstractTable<SysDict> {

    /**
     * 分组名
     */
    private String groupName;
    /**
     * 字典编码
     */
    private String code;
    /**
     * 字典名
     */
    private String name;
    /**
     * 序号
     */
    private Integer sort;

}
