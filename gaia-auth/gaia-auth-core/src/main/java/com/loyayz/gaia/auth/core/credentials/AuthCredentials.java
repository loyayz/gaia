package com.loyayz.gaia.auth.core.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthCredentials implements Serializable {
    private static final long serialVersionUID = -1L;

    private String token;

}
