package com.globallogic.barcamp.data.mapper;

import com.globallogic.barcamp.data.entity.TalkDataEntity;
import com.globallogic.barcamp.domain.Talk;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/13/2016
 */

public class TalkSnapshotMapper extends BaseSnapshotMapper<TalkDataEntity, Talk> {

    @Override
    public String getChildKey() {
        return "talks";
    }

    @Override
    public TalkDataEntity getValue(DataSnapshot snapshot) {
        TalkDataEntity value = snapshot.getValue(TalkDataEntity.class);
        if (value != null) {
            value.setPhoto(value.getPhoto());
            value.setKey(snapshot.getKey());
        }
        return value;
    }

    @Override
    public Talk map(TalkDataEntity entity) {
        return new Talk(entity.getKey(), entity.getDate(), entity.getName(), entity.getDescription(), entity.getPhoto(),
                entity.getRoomName(), entity.getSpeakerName(), entity.isDelayed(), entity.getTwitterId());
    }

    public TalkDataEntity transform(Talk talk) {
        TalkDataEntity entity = new TalkDataEntity();
        entity.setDate(talk.getDate());
        entity.setDescription(talk.getDescription());
        entity.setName(talk.getName());
        entity.setPhoto(talk.getPhoto());
        entity.setRoomName(talk.getRoomName());
        entity.setSpeakerName(talk.getSpeakerName());
        entity.setDelayed(talk.isDelayed());
        entity.setTwitterId(talk.getTwitter());
        if (talk.getId() != null) {
            entity.setKey(talk.getId());
        }
        return entity;
    }

    public List<Talk> map(List<TalkDataEntity> entityList) {
        List<Talk> result = new ArrayList<>();
        for (TalkDataEntity entity : entityList) {
            result.add(map(entity));
        }
        return result;
    }
}
