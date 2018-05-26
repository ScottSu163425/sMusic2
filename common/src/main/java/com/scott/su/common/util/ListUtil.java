package com.scott.su.common.util;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.scott.su.common.interfaces.Judgment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListUtil {
    private static final int POSITION_NONE = -1;

    private ListUtil() {
    }

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

    /**
     * 取出指定列表中指定元素的下一个元素
     *
     * @param list
     * @param currentEntity 为空时，返回列表首元素
     * @param <E>
     * @return null如果list为null或不包含任何元素
     */
    public static <E> E getNextLoop(@NonNull List<E> list, @Nullable E currentEntity) {
        if (list == null) {
            return null;
        }

        int size = list.size();

        if (size == 0) {
            return null;
        }

        if (size == 1) {
            return list.get(0);
        }

        if (currentEntity == null || !list.contains(currentEntity)) {
            return list.get(0);
        }

        final int indexCurrent = list.indexOf(currentEntity);
        int indexNext = indexCurrent + 1;

        if (indexNext == size) {
            indexNext = 0;
        }

        return list.get(indexNext);
    }

    /**
     * 取出指定列表中指定元素的上一个元素
     *
     * @param list
     * @param currentEntity 为空时，返回列表首元素
     * @param <E>
     * @return null如果list为null或不包含任何元素
     */
    public static <E> E getPrevLoop(@NonNull List<E> list, @Nullable E currentEntity) {
        if (list == null) {
            return null;
        }

        int size = list.size();

        if (size == 0) {
            return null;
        }

        if (size == 1) {
            return list.get(0);
        }

        if (currentEntity == null || !list.contains(currentEntity)) {
            return list.get(0);
        }

        final int indexCurrent = list.indexOf(currentEntity);
        int indexPrev = indexCurrent - 1;

        if (indexPrev == -1) {
            indexPrev = size - 1;
        }

        return list.get(indexPrev);
    }

    /**
     * 取出指定列表中指定元素的随机下一个元素
     *
     * @param list
     * @param entityExcluded
     * @param <E>
     * @return
     */
    public static <E> E getNextRandom(@NonNull List<E> list, @Nullable E entityExcluded) {
        if (list == null) {
            return null;
        }

        int size = list.size();

        if (size == 0) {
            return null;
        }

        if (size == 1) {
            return list.get(0);
        }

        int indexExcluded = POSITION_NONE;
        int indexNext;
        Random random = new Random();

        if (entityExcluded != null && list.contains(entityExcluded)) {
            indexExcluded = list.indexOf(entityExcluded);
        }

        do {
            indexNext = random.nextInt(size);
        } while (indexNext == indexExcluded);

        return list.get(indexNext);
    }


}
