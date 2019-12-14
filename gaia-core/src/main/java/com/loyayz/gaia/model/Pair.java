package com.loyayz.gaia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pair<L, R> implements Serializable {
    private static final long serialVersionUID = 1L;

    private L left;
    private R right;

    public static Pair<String, Object> simplePair(String left, Object right) {
        return new Pair<>(left, right);
    }

    public static Pair<String, String> stringPair(String left, String right) {
        return new Pair<>(left, right);
    }

}
