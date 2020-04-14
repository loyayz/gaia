package com.loyayz.gaia.data.mybatis.extension;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.loyayz.gaia.data.PageModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Pages {

    public static <T> PageModel<T> doSelectPage(int pageNum, int pageSize, Supplier<List<T>> listAction) {
        return doSelectPage(pageNum, pageSize, listAction, null);
    }

    public static <T> PageModel<T> doSelectPage(int pageNum, int pageSize, Supplier<List<T>> listAction, int count) {
        return doSelectPage(pageNum, pageSize, listAction, () -> count);
    }

    public static <T> PageModel<T> doSelectPage(int pageNum, int pageSize,
                                                Supplier<List<T>> listAction,
                                                Supplier<Integer> countAction) {
        boolean autoCount = countAction == null;
        Page<T> page = PageHelper.startPage(pageNum, pageSize, autoCount)
                .doSelectPage(listAction::get);
        if (!autoCount) {
            page.setTotal(countAction.get());
        }
        return PageModel.<T>builder()
                .items(page.getResult())
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total((int) page.getTotal())
                .pages(page.getPages())
                .offset(page.getStartRow())
                .build();
    }


}
