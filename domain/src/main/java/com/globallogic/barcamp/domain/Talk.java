package com.globallogic.barcamp.domain;

/**
 * Created by Gonzalo.Martin on 10/4/2016
 */

public class Talk {

    private String id;
    private Long date;
    private String name;
    private String description;
    private String photo;
    private String roomName;
    private String speakerName;
    private String twitter;
    private boolean delayed = false;

    public Talk(String id, Long date, String name, String description, String roomName, String speakerName, boolean delayed, String twitter) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.description = description;
        this.roomName = roomName;
        this.speakerName = speakerName;
        this.delayed = delayed;
        this.twitter = twitter;
    }

    public Talk(String id, Long date, String name, String description, String photo, String roomName, String speakerName, boolean delayed, String twitter) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.roomName = roomName;
        this.speakerName = speakerName;
        this.delayed = delayed;
        this.twitter = twitter;
    }

    public void setPhoto(String url) {
        this.photo = url;
    }

    public String getId() {
        return this.id;
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

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean isDelayed) {
        this.delayed = isDelayed;
    }

    public String getTwitter() {
        if (twitter != null && !twitter.isEmpty()) {
            return twitter;
        }
        return "";
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
