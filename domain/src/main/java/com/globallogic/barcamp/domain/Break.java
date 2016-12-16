package com.globallogic.barcamp.domain;

/**
 * Created by Gonzalo.Martin on 10/28/2016
 */
public class Break {

    public static final Integer TYPE = -1;
    private long duration;
    private long time;

    public Break(long duration, long time) {
        this.duration = duration;
        this.time = time;
    }

    public long getDuration() {
        return duration;
    }

    public long getTime() {
        return time;
    }
}
