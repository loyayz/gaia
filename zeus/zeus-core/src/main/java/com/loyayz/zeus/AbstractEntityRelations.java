package com.loyayz.zeus;

import com.loyayz.gaia.data.mybatis.BaseTable;
import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.gaia.util.ClassUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public abstract class AbstractEntityRelations<T extends BaseTable<T>> {
    private final EntityId entityId;
    /**
     * 要新增的值
     */
    private final Set<Long> newItems = new HashSet<>();
    /**
     * 要删除的值
     */
    private final Set<Long> deletedItems = new HashSet<>();

    /**
     * 已存在数据库中的值
     */
    protected abstract List<Long> existInRepo(Set<Long> items);

    /**
     * 构建关联对象
     */
    protected abstract T buildRelation(Long item);

    /**
     * 新增
     */
    public void add(List<Long> items) {
        this.newItems.addAll(items);
    }

    /**
     * 删除
     */
    public void remove(List<Long> items) {
        this.newItems.removeAll(items);
        this.deletedItems.addAll(items);
    }

    /**
     * 保存
     */
    public void save() {
        this.insert();
        this.delete();
    }

    protected EntityId entityId() {
        return this.entityId;
    }

    protected Set<Long> newItems() {
        return this.newItems;
    }

    protected Set<Long> deletedItems() {
        return this.deletedItems;
    }

    protected Class<T> getRelationClass() {
        return ClassUtils.resolveGenericType(getClass());
    }

    protected String deleteMapperMethod() {
        return "deleteByEntityRelation";
    }

    private void insert() {
        if (this.newItems.isEmpty()) {
            return;
        }
        List<Long> existItems = this.existInRepo(this.newItems);
        this.newItems.removeAll(existItems);

        List<T> relations = this.newItems.stream()
                .map(this::buildRelation)
                .collect(Collectors.toList());
        if (!relations.isEmpty()) {
            try {
                this.getRelationClass().newInstance().insert(relations);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        this.newItems.clear();
    }

    private void delete() {
        if (this.deletedItems.isEmpty()) {
            return;
        }
        Class<T> relationClass = this.getRelationClass();
        String methodName = this.deleteMapperMethod();
        this.deletedItems.stream()
                .map(this::buildRelation)
                .collect(Collectors.toList())
                .forEach(relation -> {
                    MybatisUtils.executeDelete(relationClass, methodName, relation);
                });
        this.deletedItems.clear();
    }

    protected AbstractEntityRelations(EntityId entityId) {
        this.entityId = entityId;
    }
}
