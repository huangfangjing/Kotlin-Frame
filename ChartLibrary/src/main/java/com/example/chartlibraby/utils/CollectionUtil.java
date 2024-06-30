package com.example.chartlibraby.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by chenhongfei on 2017/1/17.
 */
public class CollectionUtil {
    /**
     * 判断集合是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Collection obj) {
        if (obj == null || obj.isEmpty()) {
            return true;
        }

        return false;
    }

    public static boolean isEmpty(Map obj) {
        if (obj == null || obj.isEmpty()) {
            return true;
        }

        return false;
    }
}
