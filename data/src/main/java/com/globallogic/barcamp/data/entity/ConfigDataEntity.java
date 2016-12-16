package com.globallogic.barcamp.data.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

/**
 * Created by Gonzalo.Martin on 10/27/2016
 */

@IgnoreExtraProperties
public class ConfigDataEntity {

    private long eventDate;
    private long startTime;
    private long endTime;
    private long interval;
    private long serverTime;
    private HashMap<String, RoomEntity> rooms;
    private HashMap<String, BreakEntity> breaks;

    public long getEventDate() {
        return eventDate;
    }

    public void setEventDate(long eventDate) {
        this.eventDate = eventDate;
    }

    public HashMap<String, RoomEntity> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<String, RoomEntity> rooms) {
        this.rooms = rooms;
    }

    public HashMap<String, BreakEntity> getBreaks() {
        return breaks;
    }

    public void setBreaks(HashMap<String, BreakEntity> breaks) {
        this.breaks = breaks;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }
}
