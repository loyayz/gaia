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
public class UaaClientApp extends AbstractTable<UaaClientApp> {

    /**
     * 客户端
     */
    private Long clientId;
    /**
     * 应用
     */
    private Long appId;

}
