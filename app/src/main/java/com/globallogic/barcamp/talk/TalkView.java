package com.globallogic.barcamp.talk;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.globallogic.barcamp.BaseView;
import com.globallogic.barcamp.domain.Talk;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/12/2016
 */
public interface TalkView extends BaseView {
    void roomRender(List<String> roomList);

    void showBackButton();

    void setTitle();

    void renderFields(Talk data);

    void setSubmitButtonText();

    void startActivityResult(Intent cameraIntent, int takePhotoCode);

    void showPicture(String picturePath, BitmapFactory.Options options);

    void showPictureButtons();

    void hideCameraButton();

    void onBackPressed();

    void showErrorEmptyName();

    void showErrorEmptySpeaker();

    long getTalkTime();

    String getTalkRoomSelected();

    void showSavingDialog();

    void dismissSavingDialog();

    void showLostChangesConfirmation(DialogInterface.OnClickListener onClickListener);

    void addTextWatchers();

    void showDeleteImageConfirmation(DialogInterface.OnClickListener positiveListener);

    void showDeleteFailure();

    void closeKeyboard();
}
