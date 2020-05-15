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
public class UaaUserOperation extends AbstractTable<UaaUserOperation> {
    /**
     * 用户
     */
    private Long userId;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 内容
     */
    private String content;

}
