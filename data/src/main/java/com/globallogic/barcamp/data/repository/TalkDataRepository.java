package com.globallogic.barcamp.data.repository;

import com.globallogic.barcamp.data.entity.TalkDataEntity;
import com.globallogic.barcamp.data.firebase.callback.FirebaseTalkDataCallback;
import com.globallogic.barcamp.data.mapper.TalkSnapshotMapper;
import com.globallogic.barcamp.domain.Talk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gonzalo.Martin on 10/18/2016
 */

public class TalkDataRepository implements DataRepository<Talk> {

    private static final String CHILD_KEY = "talks";

    private FirebaseTalkDataCallback<List<Talk>> talkDataListCallback;
    private FirebaseTalkDataCallback<Talk> talkDataItemCallback;
    private TalkSnapshotMapper mMapper;
    private DatabaseReference mDatabaseReference;

    private ValueEventListener talkListListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // convert to Talk objects
            List<TalkDataEntity> talkDataEntities = mMapper.transform(dataSnapshot);

            // call to callback
            talkDataListCallback.onDataChanged(mMapper.map(talkDataEntities));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            talkDataListCallback.onCancelled(databaseError);
        }
    };

    private ValueEventListener talkItemListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            TalkDataEntity entity = mMapper.getValue(dataSnapshot);
            talkDataItemCallback.onDataChanged(mMapper.map(entity));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            talkDataItemCallback.onCancelled(databaseError);
        }
    };

    public TalkDataRepository() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mMapper = new TalkSnapshotMapper();
    }

    @Override
    public void create(Talk data) {
        TalkDataEntity talkDataEntity = mMapper.transform(data);
        mDatabaseReference.child(CHILD_KEY).push().setValue(talkDataEntity);
    }

    @Override
    public void update(Talk data) {
        TalkDataEntity talkDataEntity = mMapper.transform(data);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + talkDataEntity.getKey(), talkDataEntity.toMap());
        mDatabaseReference.child(CHILD_KEY).updateChildren(childUpdates);
    }

    @Override
    public void delete(Talk data) {
        // Do nothing for now...
    }

    @Override
    public void delete(String id) {
        mDatabaseReference.child(CHILD_KEY).child(id).removeValue();
    }

    public void removeValueEventListener() {
        mDatabaseReference.removeEventListener(talkListListener);
    }

    public void setTalkDataCallback(FirebaseTalkDataCallback<List<Talk>> dataCallback) {
        this.talkDataListCallback = dataCallback;
        mDatabaseReference.addValueEventListener(talkListListener);
    }

    public void setTalkDataCallback(String eventId, FirebaseTalkDataCallback<Talk> dataCallback) {
        this.talkDataItemCallback = dataCallback;
        mDatabaseReference.child(CHILD_KEY).child(eventId).addValueEventListener(talkItemListener);
    }

    public void removeTalkItemEventListener(String eventId) {
        DatabaseReference talksNode = mDatabaseReference.child(CHILD_KEY);
        if (talksNode != null) {
            DatabaseReference talkItem = talksNode.child(eventId);
            if (talkItem != null) {
                talkItem.removeEventListener(talkItemListener);
            }
        }
    }
}
