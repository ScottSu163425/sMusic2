package com.scott.su.common.util;


import com.scott.su.common.interfaces.Judgment;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    private static final int POSITION_NONE = -1;


    public static <E> boolean isExit(List<E> list, Judgment<E> judgment) {
//        return !filter(list, judgment).isEmpty();
        return getPositionIntList(list, judgment) != POSITION_NONE;
    }

    /**
     * 获取列表中符合条件的数据
     *
     * @param list
     * @param judgment
     * @param <E>
     * @return 不为null的集合
     */
    public static <E> List<E> filter(List<E> list, Judgment<E> judgment) {
        List<E> listMatched = new ArrayList<>();

        if (list != null && !list.isEmpty() && judgment != null) {
            for (int i = 0, n = list.size(); i < n; i++) {
                E item = list.get(i);

                if (judgment.test(item)) {
                    listMatched.add(item);
                }
            }
        }

        return listMatched;
    }

    /**
     * @param list
     * @param judgment
     * @param <E>
     * @return
     */
    public static <E> int getPositionIntList(List<E> list, Judgment<E> judgment) {
        if (list == null || list.isEmpty()) {
            return POSITION_NONE;
        }

        for (int i = 0, n = list.size(); i < n; i++) {
            E item = list.get(i);
            if (judgment.test(item)) {
                return i;
            }
        }

        return POSITION_NONE;
    }

}
