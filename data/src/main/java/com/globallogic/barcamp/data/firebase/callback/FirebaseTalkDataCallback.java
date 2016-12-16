package com.globallogic.barcamp.data.firebase.callback;

import com.google.firebase.database.DatabaseError;

/**
 * Created by Gonzalo.Martin on 10/13/2016
 */

public interface FirebaseTalkDataCallback<T> {
    void onDataChanged(T data);

    void onCancelled(DatabaseError error);
}
