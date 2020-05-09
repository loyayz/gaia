package com.loyayz.gaia.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 *
 * @author loyayz (loyayz@foxmail.com)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUtils {

    /**
     * 计算页数
     */
    public static int calcPages(Integer total, Integer pageSize) {
        if (pageSize <= 0) {
            return 0;
        }
        int pages = total / pageSize;
        if (total % pageSize != 0) {
            pages++;
        }
        return pages;
    }

    public static <T> List<List<T>> splitToPage(List<T> list) {
        return splitToPage(list, 200);
    }

    public static <T> List<List<T>> splitToPage(List<T> list, int pageSize) {
        List<List<T>> result = new ArrayList<>();
        if (list == null) {
            return result;
        }

        List<T> currentPage = new ArrayList<>();
        int currentPageDataNum = 0;
        for (T entity : list) {
            if (currentPageDataNum < pageSize) {
                currentPage.add(entity);
                currentPageDataNum++;
            }
            if (currentPageDataNum == pageSize) {
                result.add(currentPage);

                currentPage = new ArrayList<>();
                currentPageDataNum = 0;
            }
        }
        if (!currentPage.isEmpty()) {
            result.add(currentPage);
        }
        return result;
    }

}
