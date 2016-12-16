package com.globallogic.barcamp.data.cache;

import com.globallogic.barcamp.data.entity.TalkDataEntity;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/13/2016
 */

public class TalkDataCache extends DataCache<TalkDataEntity> {

    private static final String KEY_TALK_LIST = "talk_list";

    private static TalkDataCache INSTANCE;

    public static TalkDataCache get() {
        if (INSTANCE == null) {
            INSTANCE = new TalkDataCache();
        }
        return INSTANCE;
    }

    public void saveTalks(List<TalkDataEntity> list) {
        putValue(KEY_TALK_LIST, list);
    }

    public List<TalkDataEntity> restoreTalks() {
        return getListValue(KEY_TALK_LIST);
    }
}
