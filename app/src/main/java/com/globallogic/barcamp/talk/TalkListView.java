package com.globallogic.barcamp.talk;

import android.content.DialogInterface;
import android.view.View;

import com.globallogic.barcamp.BaseView;
import com.globallogic.barcamp.domain.Talk;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/12/2016
 */
public interface TalkListView extends BaseView {

    void showAddButton();

    void initAddButton();

    void hideAddButton();

    void setTitle(String formattedHour);

    void showBackButton();

    void render(List<Talk> data, boolean adminUser);

    void showToast(String text);

    void displayAddTalkFragment(Long talkEpochTime);

    void displayEditTalkFragment(Talk talk);

    void showEmptyView();

    void displayPopupMenu(View menuButton, Talk talk);

    void displayDeleteTalkConfirmation(String name, DialogInterface.OnClickListener confirmClickListener, DialogInterface.OnClickListener onConfirmClickListener);

    void hideKeyboard();

    void showFullRoomsMessage();

    void hideLogo();

    void updateDelayedItemMenu(boolean isDelayed);

    void displayTalkImageFragment(String name, String url);
}
