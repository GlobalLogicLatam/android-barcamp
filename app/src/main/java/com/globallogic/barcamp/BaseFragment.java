package com.globallogic.barcamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.globallogic.barcamp.home.MainActivity;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment {

    protected T presenter;

    protected abstract int getLayoutResource();

    protected abstract T buildPresenter();

    protected abstract void initView(View view);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        presenter = buildPresenter();
        initView(view);
        presenter.onCreateView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    protected void setToolbarTitle(String text) {
        ((MainActivity) getActivity()).setToolbarTitle(text);
    }

    public void notifyLogout() {
        presenter.onLogout();
    }

    public void hideBackButton() {
        ((MainActivity) getActivity()).hideToolbarBackButton();
    }

    public void showBackButton() {
        ((MainActivity) getActivity()).displayToolbarBackButton();
    }

    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    public boolean canBack() {
        return presenter.canBack();
    }
}
