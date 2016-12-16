package com.globallogic.barcamp.home.social;

import com.globallogic.barcamp.BasePresenter;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUi;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public class SocialPresenter extends BasePresenter<SocialView> {
    public SocialPresenter(SocialView view) {
        super(view);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        loadTweets();
    }

    public void loadTweets() {
        view.loadTweets();
        view.render();
    }
}
