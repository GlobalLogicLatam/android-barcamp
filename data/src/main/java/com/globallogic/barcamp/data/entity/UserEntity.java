package com.globallogic.barcamp.data.entity;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Gonzalo.Martin on 10/26/2016
 */

@IgnoreExtraProperties
public class UserEntity {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
