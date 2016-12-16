package com.globallogic.barcamp;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.LongSparseArray;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.FacebookSdk;
import com.globallogic.barcamp.domain.Config;
import com.globallogic.barcamp.domain.Room;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.TweetUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Gustavo on 9/20/16
 */
public class BarcampApplication extends Application {

    private static final String BARCAMP_PREFERENCES = "barcamp.preferences";
    private static final String KEY_USER_EMAIL = "key.user.email";

    private static BarcampApplication INSTANCE;

    private Config configData;
    private LongSparseArray<List<Room>> roomAvailabilityMap;
    private Map<String, String> urlMap;

    public static BarcampApplication get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        roomAvailabilityMap = new LongSparseArray<>();
        urlMap = new HashMap<>();

        // init facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        boolean isDebug = "debug".equals(BuildConfig.BUILD_TYPE);
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(isDebug).build())
                .build();

        // enable Twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);

        // enable Twitter
        Fabric fabric = new Fabric.Builder(this).debuggable(true)
                .kits(crashlyticsKit,
                        new TwitterCore(authConfig),
                        new TweetUi(),
                        new TweetComposer())
                .build();

        // Initialize Fabric
        Fabric.with(fabric);

        INSTANCE = this;
    }

    public void setConfig(Config config) {
        this.configData = config;
    }

    public Config getConfigData() {
        return configData;
    }

    public void addTakenRoom(long time, Room room) {
        List<Room> rooms = getTakenRooms(time);
        if (rooms == null) {
            rooms = new ArrayList<>();
        }
        if (!rooms.contains(room)) {
            rooms.add(room);
            roomAvailabilityMap.put(time, rooms);
        }
    }

    public void clearTakenRooms() {
        roomAvailabilityMap.clear();
    }

    public boolean isTakenRoom(long time, Room room) {
        List<Room> rooms = getTakenRooms(time);
        if (rooms != null && !rooms.isEmpty()) {
            return rooms.contains(room);
        }
        return false;
    }

    public List<Room> getTakenRooms(long time) {
        return roomAvailabilityMap.get(time);
    }

    public void addImageUrl(String formattedUrl, String finalUrl) {
        urlMap.put(formattedUrl, finalUrl);
    }

    public boolean containsUrl(String formattedUrl) {
        return urlMap.containsKey(formattedUrl);
    }

    public String getFinalUrl(String formattedUrl) {
        return urlMap.get(formattedUrl);
    }

    public void setUserEmail(String userEmail) {
        SharedPreferences.Editor editor = getSharedPreferences(BARCAMP_PREFERENCES, MODE_PRIVATE).edit();
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.apply();
    }

    public String getSavedEmail() {
        return getSharedPreferences(BARCAMP_PREFERENCES, MODE_PRIVATE).getString(KEY_USER_EMAIL, null);
    }
}
