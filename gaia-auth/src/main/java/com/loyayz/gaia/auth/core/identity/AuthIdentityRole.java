package com.loyayz.gaia.auth.core.identity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthIdentityRole implements Serializable {
    private static final long serialVersionUID = -1L;

    private String code;
    private String name;

}
