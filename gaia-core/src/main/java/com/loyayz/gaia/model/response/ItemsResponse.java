package com.loyayz.gaia.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemsResponse<T> implements Serializable {
    private static final long serialVersionUID = -1L;

    private int status = 200;
    private List<T> items;

    public static <R> ItemsResponse<R> success(List<R> data) {
        return new ItemsResponse<>(200, data);
    }

}
