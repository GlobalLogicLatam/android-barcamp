package com.globallogic.barcamp.domain;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/28/2016
 */

public class Config {

    private long eventDate;
    private long startTime;
    private long endTime;
    private long interval;
    private long serverTime;
    private List<Room> rooms;
    private List<Break> breaks;

    public Config(long startTime, long endTime, long interval, long serverTime, long eventDate, List<Room> rooms, List<Break> breaks) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.interval = interval;
        this.serverTime = serverTime;
        this.eventDate = eventDate;
        this.rooms = rooms;
        this.breaks = breaks;
    }

    public long getEventDate() {
        return eventDate;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Break> getBreaks() {
        return breaks;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getInterval() {
        return interval;
    }

    public long getServerTime() {
        return serverTime;
    }
}
