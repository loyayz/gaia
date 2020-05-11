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
public class UaaRole extends AbstractEntity<UaaRole> {

    /**
     * 角色编码
     */
    private String code;
    /**
     * 角色名
     */
    private String name;

}
