package com.loyayz.zeus;

import com.loyayz.gaia.data.mybatis.BaseTable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class AbstractEntity<T extends BaseTable<T>> implements Entity {
    private T entity;
    private boolean updated = false;

    /**
     * 构建实体
     * 从数据库表查询或根据 dto 创建
     */
    protected abstract T buildEntity();

    protected void entity(T e) {
        this.entity = e;
    }

    protected T entity() {
        if (this.entity == null) {
            this.entity = this.buildEntity();
        }
        return this.entity;
    }

    @Override
    public void save() {
        if (this.updated()) {
            this.entity().save();
        }
    }

    @Override
    public void delete() {
        this.entity().deleteById();
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
