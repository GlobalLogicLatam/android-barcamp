package com.globallogic.barcamp.talk;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.globallogic.barcamp.BaseFragment;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.domain.Talk;
import com.globallogic.barcamp.home.MainActivity;
import com.globallogic.barcamp.widget.behavior.ScrollAwareFABBehavior;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/12/2016
 */

public class TalkListFragment extends BaseFragment<TalkListPresenter> implements TalkListView {

    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private Long talkTime;
    private View emptyView;
    private PopupMenu popupMenu;

    public static TalkListFragment newInstance(Long talkTime) {
        TalkListFragment talkListFragment = new TalkListFragment();
        talkListFragment.setTalkTime(talkTime);
        return talkListFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_talk_list;
    }

    @Override
    protected TalkListPresenter buildPresenter() {
        return new TalkListPresenter(this, talkTime);
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        addButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        emptyView = view.findViewById(R.id.empty_view);

        // Setup recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // set background tint
        ViewCompat.setBackgroundTintList(addButton, ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
    }

    @Override
    public void showAddButton() {
        addButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void initAddButton() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAddButtonPressed();
            }
        });
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) addButton.getLayoutParams();
        p.setBehavior(new ScrollAwareFABBehavior(getContext()));
        addButton.setLayoutParams(p);
    }

    @Override
    public void hideAddButton() {
        addButton.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(String formattedHour) {
        setToolbarTitle(getString(R.string.talk_list_screen_title, formattedHour));
    }

    @Override
    public void render(List<Talk> data, boolean adminUser) {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new TalkAdapter(getContext(), data, adminUser, presenter));
    }

    @Override
    public void displayAddTalkFragment(Long talkEpochTime) {
        ((MainActivity) getActivity()).setAddTalkFragment(talkEpochTime);
    }

    @Override
    public void displayEditTalkFragment(Talk talk) {
        ((MainActivity) getActivity()).setEditTalkFragment(talk);
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void displayPopupMenu(View view, Talk talk) {
        popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(presenter);
        popupMenu.setOnDismissListener(presenter);
        popupMenu.inflate(R.menu.talk_item_menu);
        updateDelayedItemMenu(talk.isDelayed());
        popupMenu.show();
    }

    @Override
    public void displayDeleteTalkConfirmation(String name, DialogInterface.OnClickListener confirmClickListener,
                                              DialogInterface.OnClickListener dismissClickListener) {
        // build AlertDialog
        AlertDialog deleteConfirmationDialog = new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.delete_confirmation_message, name))
                .setPositiveButton(R.string.delete, confirmClickListener)
                .setNegativeButton(android.R.string.no, dismissClickListener)
                .setCancelable(false)
                .create();

        // show dialog
        deleteConfirmationDialog.show();
    }

    @Override
    public void hideKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void showFullRoomsMessage() {
        showToast(getString(R.string.full_rooms_message));
    }

    @Override
    public void hideLogo() {
        ((MainActivity) getActivity()).hideToolbarLogo();
    }

    @Override
    public void updateDelayedItemMenu(boolean isDelayed) {
        if (popupMenu != null) {
            MenuItem menuItem = popupMenu.getMenu().getItem(2); // Delayed Item
            if (menuItem != null) {
                menuItem.setTitle(isDelayed ? getString(R.string.not_delayed) : getString(R.string.delayed));
            }
        }
    }

    @Override
    public void displayTalkImageFragment(String name, String imageUrl) {
        ((MainActivity) getActivity()).setTalkImageFragment(name, imageUrl);
    }

    private void setTalkTime(Long talkTime) {
        this.talkTime = talkTime;
    }
}
