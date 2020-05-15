package com.loyayz.zeus;

import com.loyayz.gaia.data.mybatis.BaseTable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class AbstractEntity<T extends BaseTable<T>> implements Entity {

    private boolean updated = false;

    /**
     * 数据库表实体
     */
    protected abstract T table();

    @Override
    public void save() {
        if (this.updated()) {
            this.table().save();
        }
    }

    @Override
    public void delete() {
        this.table().deleteById();
    }

    protected void markUpdated() {
        this.updated = true;
    }

    /**
     * 是否更新过
     */
    protected boolean updated() {
        return this.updated;
    }

}
