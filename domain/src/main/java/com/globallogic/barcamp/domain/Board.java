package com.globallogic.barcamp.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gonzalo.Martin on 10/4/2016
 */

public class Board {

    public static final Integer BREAK_TYPE = Break.TYPE;
    public static final Integer REGISTER_TYPE = -2;

    private Map<Long, Integer> map;

    public Board() {
        this.map = new LinkedHashMap<>();
    }

    public void addHour(Long hour, Integer count) {
        map.put(hour, count);
    }

    public void addBreak(Long hour) {
        addHour(hour, BREAK_TYPE);
    }

    public void addRegister(Long hour) {
        addHour(hour, REGISTER_TYPE);
    }

    public Integer getCount(Long hour) {
        return map.get(hour);
    }

    public int size() {
        return this.map.size();
    }

    public List<Map.Entry<Long, Integer>> getEntryList() {
        return new ArrayList<>(map.entrySet());
    }
}
