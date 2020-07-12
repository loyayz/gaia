package com.loyayz.uaa.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@Setter
public class AccountPasswordIncorrectException extends UaaException {
    private String accountType;
    private String accountName;

    public AccountPasswordIncorrectException(String accountType, String accountName) {
        super("Account password incorrect! [" + accountType + " - " + accountName + "]");
    }

}
