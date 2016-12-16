package com.globallogic.barcamp.data.mapper;

import com.globallogic.barcamp.data.entity.UserEntity;
import com.globallogic.barcamp.domain.User;
import com.google.firebase.database.DataSnapshot;

/**
 * Created by Gonzalo.Martin on 10/26/2016
 */
public class UserSnapshotMapper extends BaseSnapshotMapper<UserEntity, User> {

    @Override
    public String getChildKey() {
        return "emails";
    }

    @Override
    public UserEntity getValue(DataSnapshot snapshot) {
        return snapshot.getValue(UserEntity.class);
    }

    @Override
    public User map(UserEntity entity) {
        return new User(entity.getEmail());
    }

    public UserEntity transform(User user) {
        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail());
        return entity;
    }
}
