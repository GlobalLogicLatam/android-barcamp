package com.globallogic.barcamp.data.firebase;

import android.app.Activity;
import android.util.Log;

import com.globallogic.barcamp.data.BuildConfig;
import com.globallogic.barcamp.data.firebase.callback.ConfigDataCallback;
import com.globallogic.barcamp.data.firebase.callback.DeleteFileStorageCallback;
import com.globallogic.barcamp.data.firebase.callback.FirebaseAuthCallback;
import com.globallogic.barcamp.data.firebase.callback.FirebaseTalkDataCallback;
import com.globallogic.barcamp.data.firebase.callback.UploadFileStorageCallback;
import com.globallogic.barcamp.data.firebase.callback.UserDataCallback;
import com.globallogic.barcamp.data.repository.ConfigDataRepository;
import com.globallogic.barcamp.data.repository.StorageDataRepository;
import com.globallogic.barcamp.data.repository.TalkDataRepository;
import com.globallogic.barcamp.data.repository.UserDataRepository;
import com.globallogic.barcamp.domain.Talk;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Gonzalo.Martin on 10/12/2016
 */

public class FirebaseWorker {

    private static final String TAG = FirebaseWorker.class.getSimpleName();
    public static final String FIREBASE_PATH = BuildConfig.FIREBASE_ROOT;

    private TalkDataRepository talkDataRepository;
    private UserDataRepository userDataRepository;
    private StorageDataRepository storageDataRepository;
    private ConfigDataRepository configDataRepository;

    private FirebaseAuth mAuth;
    private boolean isLoggedIn = false;

    public FirebaseWorker() {
        mAuth = FirebaseAuth.getInstance();
        talkDataRepository = new TalkDataRepository();
        userDataRepository = new UserDataRepository();
        storageDataRepository = new StorageDataRepository();
        configDataRepository = new ConfigDataRepository();
        Log.d(TAG, "Firebase Initialized: " + FIREBASE_PATH);
    }

    public void addAuthListener(FirebaseAuth.AuthStateListener mAuthListener) {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void removeAuthListener(FirebaseAuth.AuthStateListener mAuthStateListener) {
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    public boolean isAdminUser() {
        return mAuth.getCurrentUser() != null && !mAuth.getCurrentUser().isAnonymous();
    }

    public void loginFirebase(Activity currentActivity, final FirebaseAuthCallback authCallback, String email, String password) {
        // login as registered user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(currentActivity, new AuthResultComplete(authCallback));
    }

    public void loginFirebase(Activity currentActivity, final FirebaseAuthCallback authCallback) {
        // login as anonymous
        mAuth.signInAnonymously().addOnCompleteListener(currentActivity, new AuthResultComplete(authCallback));
    }

    public void removeTalkEventListener() {
        talkDataRepository.removeValueEventListener();
    }

    public void addTalkEventListener(FirebaseTalkDataCallback dataCallback) {
        talkDataRepository.setTalkDataCallback(dataCallback);
    }

    public void addTalkEventListener(String eventId, FirebaseTalkDataCallback dataCallback) {
        talkDataRepository.setTalkDataCallback(eventId, dataCallback);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void add(Talk talk) {
        talkDataRepository.create(talk);
    }

    public void update(Talk talk) {
        talkDataRepository.update(talk);
    }

    public void removeValue(String valueId) {
        talkDataRepository.delete(valueId);
    }

    public void uploadStorageItem(String localFilePath, UploadFileStorageCallback callback) {
        storageDataRepository.uploadFile(localFilePath, callback);
    }

    public void deleteStorageItem(String name, DeleteFileStorageCallback callback) {
        storageDataRepository.deleteFile(name, callback);
    }

    public void addGuestEmail(String userEmail, UserDataCallback callback) {
        userDataRepository.addUserEmail(userEmail, callback);
    }

    public void removeEmailEventListener() {
        userDataRepository.removeEmailEventListener();
    }

    public void getConfig(ConfigDataCallback callback) {
        configDataRepository.get(callback);
    }

    public void removeConfigEventListener() {
        configDataRepository.removeEventListener();
    }

    public void logout() {
        mAuth.signOut();
    }

    public void removeTalkItemEventListener(String eventId) {
        talkDataRepository.removeTalkItemEventListener(eventId);
    }
}
