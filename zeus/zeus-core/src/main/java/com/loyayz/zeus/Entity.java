package com.loyayz.zeus;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface Entity<ID> {

    /**
     * 保存（修改或修改）
     */
    ID save();

    /**
     * 删除
     */
    void delete();

}
