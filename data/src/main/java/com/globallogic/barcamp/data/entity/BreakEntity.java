package com.globallogic.barcamp.data.entity;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Gonzalo.Martin on 10/27/2016
 */

@IgnoreExtraProperties
public class BreakEntity {

    private long duration;
    private long time;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
