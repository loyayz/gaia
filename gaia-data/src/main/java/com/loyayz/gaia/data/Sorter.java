package com.loyayz.gaia.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
public class Sorter implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 是否是 ASC 排序
     */
    private boolean asc;
    /**
     * 排序字段
     */
    private String column;
    /**
     * 非法字符串
     */
    private List<String> illegalString = Collections.singletonList(";");

    public static Sorter asc(String column) {
        Sorter sorter = new Sorter();
        sorter.setAsc(true);
        sorter.setColumn(column);
        return sorter;
    }

    public static Sorter desc(String column) {
        Sorter sorter = new Sorter();
        sorter.setAsc(false);
        sorter.setColumn(column);
        return sorter;
    }

    public static List<Sorter> ascList(String... columns) {
        return Arrays.stream(columns).map(Sorter::asc).collect(Collectors.toList());
    }

    public static List<Sorter> descList(String... columns) {
        return Arrays.stream(columns).map(Sorter::desc).collect(Collectors.toList());
    }

    private static Sorter build(String column, boolean asc) {
        Sorter sorter = new Sorter();
        sorter.setAsc(asc);
        sorter.setColumn(column);
        return sorter;
    }

    @Override
    public String toString() {
        this.valid();
        String order = asc ? "asc" : "desc";
        return column + " " + order;
    }

    private void valid() {
        if (column == null || column.isEmpty()) {
            throw new IllegalArgumentException("Sorter.column is empty");
        }
        for (String str : illegalString) {
            if (column.contains(str)) {
                throw new IllegalArgumentException("Sorter.column is illegal");
            }
        }
    }

}
