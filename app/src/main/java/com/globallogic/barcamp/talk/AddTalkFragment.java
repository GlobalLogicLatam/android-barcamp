package com.globallogic.barcamp.talk;

import com.globallogic.barcamp.R;

/**
 * Created by Gonzalo.Martin on 10/12/2016
 */
public class AddTalkFragment extends TalkFragment {

    public static AddTalkFragment newInstance(Long talkEpochTime) {
        AddTalkFragment fragment = new AddTalkFragment();
        fragment.setTalkTime(talkEpochTime);
        return fragment;
    }

    @Override
    protected TalkPresenter.TalkMode getTalkMode() {
        return TalkPresenter.TalkMode.ADD;
    }

    @Override
    protected String getSubmitButtonText() {
        return getString(R.string.load_talk);
    }

    @Override
    protected boolean isDelayed() {
        return false;
    }

    @Override
    public void setTitle() {
        setToolbarTitle(getString(R.string.add_talk));
    }
}
