package com.globallogic.barcamp.data.entity;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gonzalo.Martin on 10/18/2016
 */

@IgnoreExtraProperties
public class TalkDataEntity {

    private String key;
    private Long date;
    private String name;
    private String description;
    private String photo;
    private String roomName;
    private String speakerName;
    private String twitterId;
    private boolean delayed;

    public void setKey(String key) {
        this.key = key;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String getKey() {
        return key;
    }

    public Long getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("name", name);
        result.put("description", description);
        result.put("photo", photo);
        result.put("roomName", roomName);
        result.put("speakerName", speakerName);
        result.put("delayed", delayed);
        result.put("twitterId", twitterId);
        return result;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean isDelayed) {
        this.delayed = isDelayed;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }
}
