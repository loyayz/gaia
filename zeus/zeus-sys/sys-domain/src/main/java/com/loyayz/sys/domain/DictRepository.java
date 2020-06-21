package com.loyayz.sys.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.sys.data.SysDict;
import com.loyayz.sys.data.SysDictItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class DictRepository {

    public static SysDict findByCode(String dictCode) {
        List<SysDict> dicts = SysDict.builder().code(dictCode).build().listByCondition();
        return dicts.isEmpty() ? null : dicts.get(0);
    }

    /**
     * {@link com.loyayz.sys.data.mapper.SysDictMapper#getMaxSortByGroup}
     */
    public static Integer getDictNextSort(String groupName) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("groupName", groupName);
        Integer sort = MybatisUtils.executeSelectOne(SysDict.class, "getMaxSortByGroup", param);
        return sort == null ? 0 : sort + 1;
    }

    /**
     * {@link com.loyayz.sys.data.mapper.SysDictItemMapper#getMaxSortByCode}
     */
    public static Integer getItemNextSort(String dictCode) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("dictCode", dictCode);
        Integer sort = MybatisUtils.executeSelectOne(SysDictItem.class, "getMaxSortByCode", param);
        return sort == null ? 0 : sort + 1;
    }

}
