package com.loyayz.gaia.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
public class PageRequest implements Serializable {
    private static final long serialVersionUID = -1L;

    private static final Integer DEFAULT_NUM = 1;
    private static final Integer DEFAULT_SIZE = 20;

    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页数量
     */
    private Integer pageSize;

    public Integer getPageNum() {
        return this.pageNum != null && this.pageNum > 0 ? this.pageNum : DEFAULT_NUM;
    }

    public Integer getPageSize() {
        return this.pageSize != null && this.pageSize > 0 ? this.pageSize : DEFAULT_SIZE;
    }

    public Integer getPageStartIndex() {
        return (this.getPageNum() - 1) * this.getPageSize();
    }

}
