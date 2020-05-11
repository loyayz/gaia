package com.loyayz.uaa.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountExistException extends AbstractUaaException {

    private String type;
    private String name;

    public AccountExistException(String type, String name) {
        this.type = type;
        this.name = name;
    }

}
