package com.globallogic.barcamp.home.board;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.globallogic.barcamp.BaseActivity;
import com.globallogic.barcamp.BaseFragment;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.domain.Board;
import com.globallogic.barcamp.home.MainActivity;
import com.globallogic.barcamp.widget.DividerItemDecoration;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public class BoardFragment extends BaseFragment<BoardPresenter> implements BoardView {

    private RecyclerView recyclerView;
    private View loadingView;
    private View emptyView;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_board;
    }

    @Override
    protected BoardPresenter buildPresenter() {
        return new BoardPresenter(this);
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        loadingView = view.findViewById(R.id.loading_view);
        emptyView = view.findViewById(R.id.empty_view);

        // Setup recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void render(final Board data) {
        if (data != null) {
            emptyView.setVisibility(View.GONE);
            recyclerView.setAdapter(new BoardAdapter(data, presenter));
        }
    }

    @Override
    public Activity getCurrentActivity() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void dismissLoading() {
        loadingView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(Long talkTime) {
        ((MainActivity) getActivity()).setTalkListFragment(talkTime);
    }

    @Override
    public void renderEmpty() {
        loadingView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void forceRestartApp() {
        ((BaseActivity) getActivity()).restart();
    }
}
