package com.loyayz.sys.domain.dict;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.sys.common.dto.SimpleDictItem;
import com.loyayz.sys.data.SysDictItem;
import com.loyayz.sys.domain.DictRepository;
import com.loyayz.zeus.EntityId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
class DictItems {
    private final EntityId dictId;
    private final List<SimpleDictItem> newItems = new ArrayList<>();
    private final List<Long> deletedItems = new ArrayList<>();

    static DictItems of(EntityId dictId) {
        return new DictItems(dictId);
    }

    void add(SimpleDictItem item) {
        this.newItems.add(item);
    }

    void remove(List<Long> itemIds) {
        this.deletedItems.addAll(itemIds);
    }

    void save() {
        this.insert();
        this.delete();
    }

    private void insert() {
        if (this.newItems.isEmpty()) {
            return;
        }
        int nextSort = DictRepository.getItemNextSort(this.dictId.get());

        List<SysDictItem> items = new ArrayList<>();
        for (SimpleDictItem item : this.newItems) {
            SysDictItem entity = this.entity(item);

            Integer sort = entity.getSort();
            if (sort == null) {
                entity.setSort(nextSort++);
            } else if (sort >= nextSort) {
                nextSort = sort + 1;
            }
            items.add(entity);
        }
        new SysDictItem().insert(items);
        this.newItems.clear();
    }

    /**
     * {@link com.loyayz.sys.data.mapper.SysDictItemMapper#deleteByCodeItems}
     */
    private void delete() {
        if (this.deletedItems.isEmpty()) {
            return;
        }
        Map<String, Object> param = new HashMap<>(3);
        param.put("dictCode", this.dictId.get());
        param.put("ids", this.deletedItems);
        MybatisUtils.executeDelete(SysDictItem.class, "deleteByCodeItems", param);
        this.deletedItems.clear();
    }

    private SysDictItem entity(SimpleDictItem item) {
        return SysDictItem.builder()
                .dictCode(this.dictId.get())
                .name(item.getName())
                .value(item.getValue())
                .sort(item.getSort())
                .build();
    }

    private DictItems(EntityId dictId) {
        this.dictId = dictId;
    }
}
