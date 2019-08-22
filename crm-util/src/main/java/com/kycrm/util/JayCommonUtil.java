package com.kycrm.util;

import java.util.ArrayList;
import java.util.List;

/** 
* @author wy
* @version 创建时间：2018年1月22日 下午4:25:55
*/
public class JayCommonUtil {
    /**
     * 将集合拆分成指定长度的多个集合
     * @author: wy
     * @time: 2018年1月22日 下午4:26:02
     * @param list
     * @param len
     * @return
     */
    public static <T>List<List<T>> splitList(List<T> list, int len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }

        List<List<T>> result = new ArrayList<List<T>>();

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }
}
