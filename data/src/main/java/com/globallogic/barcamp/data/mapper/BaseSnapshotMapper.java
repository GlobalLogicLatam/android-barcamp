package com.globallogic.barcamp.data.mapper;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/26/2016
 */

public abstract class BaseSnapshotMapper<Entity, Domain> {

    public abstract String getChildKey();

    public abstract Entity getValue(DataSnapshot value);

    public abstract Domain map(Entity entity);

    public List<Entity> transform(DataSnapshot dataSnapshot) {
        Iterator<DataSnapshot> iterator = dataSnapshot.child(getChildKey()).getChildren().iterator();
        List<Entity> list = new ArrayList<>();

        // map to Talk object
        while (iterator.hasNext()) {
            DataSnapshot snapshot = iterator.next();
            Entity value = getValue(snapshot);
            if (value != null) {
                list.add(value);
            }
        }
        return list;
    }

}
