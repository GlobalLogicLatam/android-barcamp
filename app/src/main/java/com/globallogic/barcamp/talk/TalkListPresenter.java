package com.globallogic.barcamp.talk;

import android.content.DialogInterface;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.globallogic.barcamp.BarcampApplication;
import com.globallogic.barcamp.BasePresenter;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.data.firebase.FirebaseWorker;
import com.globallogic.barcamp.data.firebase.callback.FirebaseTalkDataCallback;
import com.globallogic.barcamp.domain.Room;
import com.globallogic.barcamp.domain.Talk;
import com.globallogic.barcamp.utils.StringUtils;
import com.google.firebase.database.DatabaseError;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/12/2016
 */
public class TalkListPresenter extends BasePresenter<TalkListView> implements TalkAdapter.TalkCallback, PopupMenu.OnMenuItemClickListener, PopupMenu.OnDismissListener {

    private FirebaseWorker firebaseWorker = new FirebaseWorker();
    private Long talkEpochTime;
    private Talk selectedTalk = null;
    private boolean isDeleting = false;
    private int talksAmount = 0;

    public TalkListPresenter(TalkListView view, Long talkEpochTime) {
        super(view);
        this.talkEpochTime = talkEpochTime;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        // set title
        setTitle();

        // show back button
        view.showBackButton();

        // hide toolbar logo
        view.hideLogo();

        // check for admin user
        if (isAdminUser()) {
            view.showAddButton();
            view.initAddButton();
        } else {
            view.hideAddButton();
        }

        // hide soft keyboard
        view.hideKeyboard();

        getTalks();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseWorker.removeTalkEventListener();
    }

    private void setTitle() {
        // check null based on some crashes at this point
        if (talkEpochTime != null) {
            String formattedHour = StringUtils.formatEpochTimeToHumanReadable(talkEpochTime);
            view.setTitle(formattedHour);
        }
    }

    private boolean isAdminUser() {
        return firebaseWorker.isAdminUser();
    }

    private void getTalks() {
        firebaseWorker.addTalkEventListener(new FirebaseTalkDataCallback<List<Talk>>() {
            @Override
            public void onDataChanged(List<Talk> data) {
                // filter by time
                Iterator<Talk> iterator = data.iterator();
                while (iterator.hasNext()) {
                    Talk talk = iterator.next();
                    if (!talk.getDate().equals(talkEpochTime)) {
                        iterator.remove();
                    }
                }

                updateTakenRooms(data);

                // keep the talks amount
                talksAmount = data.size();

                if (data.isEmpty()) {
                    view.showEmptyView();
                } else {
                    view.render(data, isAdminUser());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Do nothing for now..
            }
        });
    }

    public void onAddButtonPressed() {
        // check for full rooms state
        if (talksAmount == BarcampApplication.get().getConfigData().getRooms().size()) {
            view.showFullRoomsMessage();
        } else {
            view.displayAddTalkFragment(talkEpochTime);
        }
    }

    @Override
    public void onTalkMenuSelected(View menuButton, Talk talk) {
        // display PopupMenu
        view.displayPopupMenu(menuButton, talk);
        selectedTalk = talk;
    }

    @Override
    public void onTalkImageSelected(String name, String url) {
        view.displayTalkImageFragment(name, url);
    }

    // region PopupMenu listener implementations
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit:
                // open talk edition
                isDeleting = false;
                view.displayEditTalkFragment(selectedTalk);
                return true;
            case R.id.menu_item_delete:
                // display delete confirmation
                isDeleting = true;
                view.displayDeleteTalkConfirmation(selectedTalk.getName(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // delete selected talk
                                firebaseWorker.removeValue(selectedTalk.getId());
                                selectedTalk = null;
                                dialogInterface.dismiss();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isDeleting = false;
                                selectedTalk = null;
                                dialogInterface.dismiss();
                            }
                        });
                return true;
            case R.id.menu_item_delayed:
                isDeleting = false;
                selectedTalk.setDelayed(!selectedTalk.isDelayed());
                firebaseWorker.update(selectedTalk);
                view.updateDelayedItemMenu(selectedTalk.isDelayed());
                return true;
        }
        return false;
    }

    @Override
    public void onDismiss(PopupMenu menu) {
        if (!isDeleting) {
            selectedTalk = null;
        }
    }
    // endregion


    @Override
    public void onLogout() {
        super.onLogout();
        firebaseWorker.removeTalkEventListener();
    }

    /**
     * Updates the current taken rooms in memory
     * @param talks
     */
    private void updateTakenRooms(List<Talk> talks) {
        BarcampApplication.get().clearTakenRooms();
        for (Talk talk : talks) {
            BarcampApplication.get().addTakenRoom(talkEpochTime, new Room(talk.getRoomName()));
        }
    }
}
