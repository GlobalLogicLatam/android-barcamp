package com.globallogic.barcamp.data.cache;

import com.globallogic.barcamp.data.entity.UserEntity;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/26/2016
 */
public class UserDataCache extends DataCache<UserEntity> {

    private static final String KEY_USER_LIST = "user_list";

    private static UserDataCache INSTANCE;

    public static UserDataCache get() {
        if (INSTANCE == null) {
            INSTANCE = new UserDataCache();
        }
        return INSTANCE;
    }

    public void saveUsersList(List<UserEntity> list) {
        putValue(KEY_USER_LIST, list);
    }

    public List<UserEntity> restoreUsersList() {
        return getListValue(KEY_USER_LIST);
    }

}
