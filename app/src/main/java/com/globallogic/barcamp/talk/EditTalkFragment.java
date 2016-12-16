package com.globallogic.barcamp.talk;

import com.globallogic.barcamp.BarcampApplication;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.domain.Talk;
import com.globallogic.barcamp.loader.ImageLoader;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/12/2016
 */
public class EditTalkFragment extends TalkFragment {

    private Talk currentTalk;

    public static EditTalkFragment newInstance(Talk talk) {
        EditTalkFragment fragment = new EditTalkFragment();
        fragment.setTalkTime(talk.getDate());
        fragment.setTalkId(talk.getId());
        return fragment;
    }

    @Override
    protected TalkPresenter.TalkMode getTalkMode() {
        return TalkPresenter.TalkMode.EDIT;
    }

    @Override
    protected String getSubmitButtonText() {
        return getString(R.string.save_talk);
    }

    @Override
    protected boolean isDelayed() {
        return currentTalk.isDelayed();
    }

    @Override
    public void setTitle() {
        setToolbarTitle(getString(R.string.edit_talk));
    }

    @Override
    public void renderFields(Talk data) {
        currentTalk = data;
        talkName.setText(data.getName());
        talkSpeaker.setText(data.getSpeakerName());
        if (data.getTwitter() != null && !data.getTwitter().isEmpty()) {
            talkTwitterSpeaker.setText(data.getTwitter());
        }
        talkDescription.setText(data.getDescription());
        roomSpinner.setSelection(getRoomItemSelectedPosition());

        if (presenter.hasPhoto(data.getPhoto())) {
            displayPictureContainer();
            ImageLoader.load(getContext(), BarcampApplication.get().getFinalUrl(data.getPhoto()), picture);
        } else {
            hidePictureContainer();
        }
    }

    @Override
    public String getTalkRoomSelected() {
        return currentTalk.getRoomName();
    }

    private int getRoomItemSelectedPosition() {
        List<String> availableRooms = presenter.getAvailableRooms(currentTalk.getRoomName());
        return availableRooms.indexOf(currentTalk.getRoomName());
    }
}
