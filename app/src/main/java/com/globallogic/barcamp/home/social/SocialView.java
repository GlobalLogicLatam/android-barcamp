package com.globallogic.barcamp.home.social;

import com.globallogic.barcamp.BaseView;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public interface SocialView extends BaseView {

    void render();

    void loadTweets();
}
