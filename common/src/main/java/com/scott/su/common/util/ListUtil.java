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

    public static <E> boolean isEmpty(@NonNull List<E> list) {
        return (list == null) || list.isEmpty();
    }

    public static <E> void reverse(@NonNull List<E> list) {
        if (isEmpty(list)) {
            return;
        }

        List<E> listReversed = new ArrayList<>();

        for (int i = list.size()-1; i >= 0; i--) {
            listReversed.add(list.get(i));
        }

        list.clear();
        list.addAll(listReversed);

        return;
    }

    public static <E> boolean isInList(@NonNull List<E> list, @NonNull Judgment<E> judgment) {
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
    public static <E> List<E> filter(@NonNull List<E> list, @NonNull Judgment<E> judgment) {
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
    public static <E> int getPositionIntList(@NonNull List<E> list, @NonNull Judgment<E> judgment) {
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
    public static <E> E getNextLoop(@NonNull List<E> list, @NonNull final E currentEntity) {
        if (list == null || currentEntity == null) {
            return null;
        }

        int size = list.size();

        if (size == 0) {
            return null;
        }

        boolean isExist = isInList(list, new Judgment<E>() {
            @Override
            public boolean test(E obj) {
                return obj.equals(currentEntity);
            }
        });

        if (!isExist) {
            return null;
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
    public static <E> E getPrevLoop(@NonNull List<E> list, @Nullable final E currentEntity) {
        if (list == null || currentEntity == null) {
            return null;
        }

        int size = list.size();

        if (size == 0) {
            return null;
        }

        boolean isExist = isInList(list, new Judgment<E>() {
            @Override
            public boolean test(E obj) {
                return obj.equals(currentEntity);
            }
        });

        if (!isExist) {
            return null;
        }

        final int indexCurrent = list.indexOf(currentEntity);
        int indexPrev = indexCurrent - 1;

        if (indexPrev < 0) {
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
