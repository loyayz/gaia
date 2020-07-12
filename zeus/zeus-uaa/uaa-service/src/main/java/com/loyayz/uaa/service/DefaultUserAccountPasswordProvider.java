package com.loyayz.uaa.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.loyayz.uaa.exception.AccountPasswordFormatException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Service
@Order
public class DefaultUserAccountPasswordProvider implements UserAccountPasswordProvider {

    @Override
    public String accountType() {
        return "default";
    }

    @Override
    public String encrypt(String password) {
        if (StrUtil.isBlank(password)) {
            throw new AccountPasswordFormatException();
        }
        return SecureUtil.sha256(password);
    }

    @Override
    public boolean support(String accountType) {
        return true;
    }

}
