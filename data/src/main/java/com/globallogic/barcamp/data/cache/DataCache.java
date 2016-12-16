package com.globallogic.barcamp.data.cache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gonzalo.Martin on 10/13/2016
 */

public abstract class DataCache<T> {

    private Map<String, T> data;
    private Map<String, List<T>> dataList;

    public DataCache() {
        data = new LinkedHashMap<>();
        dataList = new LinkedHashMap<>();
    }

    protected void putValue(String key, T value) {
        data.put(key, value);
    }

    protected void putValue(String key, List<T> value) {
        dataList.put(key, value);
    }

    protected T getValue(String key) {
        return data.get(key);
    }

    protected List<T> getListValue(String key) {
        return dataList.get(key);
    }
}
