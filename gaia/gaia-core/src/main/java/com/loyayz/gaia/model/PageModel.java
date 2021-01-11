package com.loyayz.gaia.model;

import com.loyayz.gaia.model.response.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageModel<T> implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 数据集
     */
    private List<T> items;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 总记录数
     */
    private Integer total;
    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 当前页偏移量（起始行）
     */
    private Long offset;

    public <R> PageModel<R> convert(Function<? super T, ? extends R> mapper) {
        PageModel<R> result = new PageModel<>(this);
        List<R> collect = this.getItems().stream().map(mapper).collect(toList());
        result.setItems(collect);
        return result;
    }

    public PageResponse<T> toResponse() {
        return PageResponse.success(this.items, this.total, this.pages);
    }

    private PageModel(PageModel other) {
        this.items = new ArrayList<>();
        this.pageNum = other.getPageNum();
        this.pageSize = other.getPageSize();
        this.total = other.getTotal();
        this.pages = other.getPages();
        this.offset = other.getOffset();
    }

}
