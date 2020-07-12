package com.loyayz.uaa.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@Setter
public class AccountExistException extends UaaException {
    private String accountType;
    private String accountName;

    public AccountExistException(String accountType, String accountName) {
        super("Account exist! [" + accountType + " - " + accountName + "]");
    }

}
