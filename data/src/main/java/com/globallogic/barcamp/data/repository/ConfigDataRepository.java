package com.globallogic.barcamp.data.repository;

import com.globallogic.barcamp.data.entity.ConfigDataEntity;
import com.globallogic.barcamp.data.firebase.callback.ConfigDataCallback;
import com.globallogic.barcamp.data.mapper.ConfigSnapshotMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Gonzalo.Martin on 10/27/2016
 */
public class ConfigDataRepository implements DataRepository<ConfigDataEntity> {

    private DatabaseReference mDatabaseReference;
    private ConfigSnapshotMapper mMapper;
    private ValueEventListener configDataEvent;

    public ConfigDataRepository() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mMapper = new ConfigSnapshotMapper();
    }

    @Override
    public void create(ConfigDataEntity data) {

    }

    @Override
    public void update(ConfigDataEntity data) {

    }

    @Override
    public void delete(ConfigDataEntity data) {

    }

    @Override
    public void delete(String id) {

    }

    public void get(final ConfigDataCallback callback) {
        configDataEvent = new ConfigDataEvent(callback);
        mDatabaseReference.addValueEventListener(configDataEvent);
        mDatabaseReference.child("config").child("serverTime").setValue(ServerValue.TIMESTAMP);
    }

    public void removeEventListener() {
        mDatabaseReference.removeEventListener(configDataEvent);
    }

    private class ConfigDataEvent implements ValueEventListener {

        private ConfigDataCallback callback;

        public ConfigDataEvent(ConfigDataCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ConfigDataEntity configDataEntity = mMapper.getValue(dataSnapshot);
            callback.onConfigDataSuccess(mMapper.map(configDataEntity));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            callback.onConfigDataFailure(databaseError.toException());
        }
    }
}
