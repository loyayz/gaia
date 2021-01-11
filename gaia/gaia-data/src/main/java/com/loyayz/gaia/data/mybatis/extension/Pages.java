package com.loyayz.gaia.data.mybatis.extension;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.loyayz.gaia.model.PageModel;
import com.loyayz.gaia.model.request.PageRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Pages {

    public static <T> PageModel<T> doSelectPage(PageRequest pageRequest, Supplier<List<T>> listAction) {
        return doSelectPage(pageRequest, listAction, null);
    }

    public static <T> PageModel<T> doSelectPage(PageRequest pageRequest, Supplier<List<T>> listAction, int total) {
        return doSelectPage(pageRequest, listAction, () -> total);
    }

    public static <T> PageModel<T> doSelectPage(PageRequest pageRequest, Supplier<List<T>> listAction, Supplier<Integer> countAction) {
        return doSelectPage(pageRequest.getPageNum(), pageRequest.getPageSize(), listAction, countAction);
    }

    public static <T> PageModel<T> doSelectPage(int pageNum, int pageSize, Supplier<List<T>> listAction) {
        return doSelectPage(pageNum, pageSize, listAction, null);
    }

    public static <T> PageModel<T> doSelectPage(int pageNum, int pageSize, Supplier<List<T>> listAction, int count) {
        return doSelectPage(pageNum, pageSize, listAction, () -> count);
    }

    public static <T> PageModel<T> doSelectPage(int pageNum, int pageSize, Supplier<List<T>> listAction, Supplier<Integer> countAction) {
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
