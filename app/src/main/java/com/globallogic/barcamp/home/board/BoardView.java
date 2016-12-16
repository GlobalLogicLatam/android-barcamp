package com.globallogic.barcamp.home.board;

import android.app.Activity;

import com.globallogic.barcamp.BaseView;
import com.globallogic.barcamp.domain.Board;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public interface BoardView extends BaseView {
    void render(Board list);

    Activity getCurrentActivity();

    void showLoading();

    void dismissLoading();

    void onItemSelected(Long talkTime);

    void renderEmpty();

    void forceRestartApp();
}
