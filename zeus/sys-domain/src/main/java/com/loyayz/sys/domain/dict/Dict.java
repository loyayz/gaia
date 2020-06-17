package com.loyayz.sys.domain.dict;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.sys.common.dto.SimpleDictItem;
import com.loyayz.sys.data.SysDict;
import com.loyayz.sys.data.SysDictItem;
import com.loyayz.sys.domain.DictRepository;
import com.loyayz.zeus.AbstractEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class Dict extends AbstractEntity<SysDict> {
    private final DictId dictId;
    private final DictItems items;

    public static Dict of(String dictCode) {
        return new Dict(dictCode);
    }

    public String id() {
        return this.dictId.get();
    }

    public Dict group(String groupName) {
        super.entity().setGroupName(groupName);
        super.markUpdated();
        return this;
    }

    public Dict name(String dictName) {
        super.entity().setName(dictName);
        super.markUpdated();
        return this;
    }

    public Dict sort(Integer sort) {
        super.entity().setSort(sort);
        super.markUpdated();
        return this;
    }

    /**
     * 添加字典项
     *
     * @param item 字典项
     */
    public Dict addItem(SimpleDictItem item) {
        this.items.add(item);
        return this;
    }

    /**
     * 删除字典项
     *
     * @param itemIds 字典项
     */
    public Dict removeItem(List<Long> itemIds) {
        this.items.remove(itemIds);
        return this;
    }

    @Override
    protected SysDict buildEntity() {
        SysDict entity = DictRepository.findByCode(this.id());
        if (entity == null) {
            entity = new SysDict();
            entity.setCode(this.id());
        }
        return entity;
    }

    @Override
    public void save() {
        if (super.updated()) {
            SysDict entity = super.entity();
            if (entity.getGroupName() == null) {
                entity.setGroupName("");
            }
            if (entity.getSort() == null) {
                entity.setSort(DictRepository.getDictNextSort(entity.getGroupName()));
            }
            entity.save();
        }
        this.items.save();
    }

    /**
     * {@link com.loyayz.sys.data.mapper.SysDictMapper#deleteByCode}
     * {@link com.loyayz.sys.data.mapper.SysDictItemMapper#deleteByCode}
     */
    @Override
    public void delete() {
        Map<String, Object> param = new HashMap<>(2);
        param.put("dictCode", this.dictId.get());

        MybatisUtils.executeDelete(SysDict.class, "deleteByCode", param);
        MybatisUtils.executeDelete(SysDictItem.class, "deleteByCode", param);
    }

    private Dict(String code) {
        this.dictId = DictId.of(code);
        this.items = DictItems.of(this.dictId);
    }

}