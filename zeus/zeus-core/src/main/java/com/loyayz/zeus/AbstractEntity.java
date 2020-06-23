package com.loyayz.zeus;

import com.loyayz.gaia.data.mybatis.BaseTable;
import com.loyayz.gaia.util.ClassUtils;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SuppressWarnings("unchecked")
public abstract class AbstractEntity<T extends BaseTable<T>, IT extends Serializable> implements Entity {
    private final EntityId id;
    private T entity;
    private boolean updated = false;

    protected AbstractEntity(IT id) {
        this.id = EntityId.of(id);
    }

    public IT idValue() {
        return this.id.get();
    }

    /**
     * 构建实体
     * 从数据库表查询或根据 dto 创建
     */
    protected abstract T buildEntity();

    protected EntityId id() {
        return this.id;
    }

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
            T entity = this.entity();
            this.fillEntityBeforeSave(entity);
            entity.save();

            if (this.id.isEmpty()) {
                this.id.set(entity.pkVal());
            }
        }
        this.saveExtra();
    }

    @Override
    public void delete() {
        try {
            T deletedEntity = (T) ClassUtils.resolveGenericType(getClass()).newInstance();
            deletedEntity.deleteById(this.idValue());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.deleteExtra();
    }

    /**
     * 在保存之前给 entity 填值
     * 用于设置默认值之类
     */
    protected void fillEntityBeforeSave(T entity) {

    }

    protected void saveExtra() {

    }

    protected void deleteExtra() {

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

    /**
     * id 是否有值
     */
    protected boolean hasId() {
        return !this.id.isEmpty();
    }

}
