package com.globallogic.barcamp.data.mapper;

import com.globallogic.barcamp.data.entity.BreakEntity;
import com.globallogic.barcamp.data.entity.ConfigDataEntity;
import com.globallogic.barcamp.data.entity.RoomEntity;
import com.globallogic.barcamp.domain.Break;
import com.globallogic.barcamp.domain.Config;
import com.globallogic.barcamp.domain.Room;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/27/2016
 */

public class ConfigSnapshotMapper extends BaseSnapshotMapper<ConfigDataEntity, Config> {
    @Override
    public String getChildKey() {
        return "config";
    }

    @Override
    public ConfigDataEntity getValue(DataSnapshot snapshot) {
        DataSnapshot child = snapshot.child(getChildKey());
        return child.getValue(ConfigDataEntity.class);
    }

    @Override
    public Config map(ConfigDataEntity configDataEntity) {
        return new Config(configDataEntity.getStartTime(), configDataEntity.getEndTime(), configDataEntity.getInterval(), configDataEntity.getServerTime(),
                configDataEntity.getEventDate(), getRooms(configDataEntity.getRooms()), getBreaks(configDataEntity.getBreaks()));
    }

    public List<Room> getRooms(HashMap<String, RoomEntity> map) {
        List<Room> rooms = new ArrayList<>();
        for (RoomEntity roomEntity : map.values()) {
            rooms.add(new Room(roomEntity.getName()));
        }
        return rooms;
    }

    public List<Break> getBreaks(HashMap<String, BreakEntity> map) {
        List<Break> breaks = new ArrayList<>();
        for (BreakEntity breakEntity : map.values()) {
            breaks.add(new Break(breakEntity.getDuration(), breakEntity.getTime()));
        }
        return breaks;
    }
}
