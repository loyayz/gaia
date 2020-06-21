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
public class UaaUserAccount extends AbstractTable<UaaUserAccount> {

    /**
     * 用户
     */
    private Long userId;
    /**
     * 账号类型
     */
    private String type;
    /**
     * 账号名
     */
    private String name;
    /**
     * 密码
     */
    private String password;

}
