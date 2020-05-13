package com.loyayz.uaa.data;

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
public class UaaUserRole extends AbstractEntity<UaaUserRole> {

    /**
     * 用户
     */
    private Long userId;
    /**
     * 角色
     */
    private String roleCode;

}
