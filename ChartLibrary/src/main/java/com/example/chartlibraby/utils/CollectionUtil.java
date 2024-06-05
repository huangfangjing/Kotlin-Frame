package com.example.chartlibraby.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;
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

    /**
     * 深度拷贝List，防止地址指向同一份
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

}
