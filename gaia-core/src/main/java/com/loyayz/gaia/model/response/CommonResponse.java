package com.loyayz.gaia.model.response;

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
public class CommonResponse<T> implements Serializable {
    private static final long serialVersionUID = -1L;

    private int status = 200;
    private T data;

    public static <R> CommonResponse<R> success() {
        return success(null);
    }

    public static <R> CommonResponse<R> success(R data) {
        return new CommonResponse<>(200, data);
    }

}
