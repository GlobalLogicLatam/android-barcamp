package com.globallogic.barcamp.data.entity;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Gonzalo.Martin on 10/27/2016
 */

@IgnoreExtraProperties
public class RoomEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
