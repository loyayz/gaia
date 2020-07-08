package com.loyayz.uaa.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class ClientSecret implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 密钥类型
     */
    private String type;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 私钥
     */
    private String privateKey;

}
