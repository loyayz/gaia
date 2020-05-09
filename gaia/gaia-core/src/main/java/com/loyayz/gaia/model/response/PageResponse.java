package com.loyayz.gaia.model.response;

import com.loyayz.gaia.util.PageUtils;
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
public class PageResponse<T> implements Serializable {
    private static final long serialVersionUID = -1L;

    private int status = 200;
    /**
     * 数据集
     */
    private List<T> items;
    /**
     * 总数
     */
    private Integer total;
    /**
     * 总页数
     */
    private Integer pages;

    public static <R> PageResponse<R> success(List<R> items, int total, int pages) {
        PageResponse<R> result = new PageResponse<>();
        result.setItems(items);
        result.setTotal(total);
        result.setPages(pages);
        return result;
    }

    public static <R> PageResponse<R> successWithPageSize(List<R> items, int total, int pageSize) {
        int pages = PageUtils.calcPages(total, pageSize);
        return success(items, total, pages);
    }

}
