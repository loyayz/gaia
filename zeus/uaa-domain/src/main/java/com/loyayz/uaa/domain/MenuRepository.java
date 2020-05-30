package com.loyayz.uaa.domain;

import com.loyayz.gaia.data.mybatis.extension.MybatisUtils;
import com.loyayz.uaa.data.UaaMenu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class MenuRepository {

    public static UaaMenu findById(Long menuId) {
        return new UaaMenu().findById(menuId);
    }

    /**
     * {@link com.loyayz.uaa.data.mapper.UaaMenuMapper#getMaxSortByParent}
     */
    public static Integer getNextSort(Long parentId) {
        Map<String, Object> param = new HashMap<>(2);
        param.put("pid", parentId);
        Integer sort = MybatisUtils.executeSelectOne(UaaMenu.class, "getMaxSortByParent", param);
        return sort == null ? 0 : sort + 1;
    }

}
