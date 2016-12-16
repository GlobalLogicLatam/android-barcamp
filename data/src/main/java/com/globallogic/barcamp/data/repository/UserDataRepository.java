package com.globallogic.barcamp.data.repository;

import android.util.Log;

import com.globallogic.barcamp.data.cache.UserDataCache;
import com.globallogic.barcamp.data.entity.UserEntity;
import com.globallogic.barcamp.data.firebase.callback.UserDataCallback;
import com.globallogic.barcamp.data.mapper.UserSnapshotMapper;
import com.globallogic.barcamp.domain.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/26/2016
 */
public class UserDataRepository implements DataRepository<User> {

    private static final String TAG = UserDataRepository.class.getSimpleName();

    private UserSnapshotMapper mMapper;
    private DatabaseReference mDatabaseReference;
    private EmailEvent emailEvent;

    public UserDataRepository() {
        this.mMapper = new UserSnapshotMapper();
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void create(User data) {
        UserEntity entity = mMapper.transform(data);
        mDatabaseReference.child(mMapper.getChildKey()).push().setValue(entity);
    }

    @Override
    public void update(User data) {

    }

    @Override
    public void delete(User data) {

    }

    @Override
    public void delete(String id) {

    }

    public void addUserEmail(final String userEmail, final UserDataCallback callback) {
        emailEvent = new EmailEvent(userEmail, callback);
        this.mDatabaseReference.addValueEventListener(emailEvent);
    }

    private void addEmail(String email) {
        Log.i(TAG, "Trying to add User: " + email);
        User user = new User(email);
        if (!exists(user)) {
            Log.i(TAG, "User: " + email + " does not exist. It will be added to database");
            create(user);
        }
    }

    private boolean exists(User user) {
        List<UserEntity> userEntities = UserDataCache.get().restoreUsersList();
        if (userEntities != null && !userEntities.isEmpty()) {
            boolean exists = false;
            int pos = 0;
            while (pos < userEntities.size() && !exists) {
                exists = user.getEmail().equals(userEntities.get(pos).getEmail());
                pos++;
            }
            return exists;
        }
        return false;
    }

    public void removeEmailEventListener() {
        this.mDatabaseReference.removeEventListener(emailEvent);
    }

    private class EmailEvent implements ValueEventListener {

        private String email;
        private UserDataCallback callback;
        private boolean dataChanged = false;

        public EmailEvent(String email, UserDataCallback callback) {
            this.email = email;
            this.callback = callback;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // convert to Talk objects
            List<UserEntity> userDataEntities = mMapper.transform(dataSnapshot);

            // save in cache
            UserDataCache.get().saveUsersList(userDataEntities);

            Log.i(TAG, "Users updated success");

            if (!dataChanged) {
                dataChanged = true;
                addEmail(email);
                callback.onUserDataSaveCompleted();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(TAG, "Users could not be updated", databaseError.toException());
            if (!dataChanged) {
                callback.onUserDataSaveCompleted();
            }
        }
    }
}
