package com.scott.su.common.adapter;

/**
 * 描述:
 * 作者: su
 * 日期: 2017/10/31 15:42
 */

public interface Judgment<O> {
    boolean test(O obj);
}
