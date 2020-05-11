package com.loyayz.uaa.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleExistException extends AbstractUaaException {
    private String roleCode;

}
