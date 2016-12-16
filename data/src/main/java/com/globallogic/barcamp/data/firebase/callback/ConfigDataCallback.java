package com.globallogic.barcamp.data.firebase.callback;

import com.globallogic.barcamp.domain.Config;

/**
 * Created by Gonzalo.Martin on 10/27/2016
 */
public interface ConfigDataCallback {
    void onConfigDataSuccess(Config config);

    void onConfigDataFailure(Throwable error);
}
