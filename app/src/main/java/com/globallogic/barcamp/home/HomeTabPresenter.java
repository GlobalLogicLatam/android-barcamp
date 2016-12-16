package com.globallogic.barcamp.home;

import android.support.v4.view.ViewPager;

import com.globallogic.barcamp.BasePresenter;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public class HomeTabPresenter extends BasePresenter<HomeTabView> implements ViewPager.OnPageChangeListener {

    public HomeTabPresenter(HomeTabView view) {
        super(view);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        view.setTitle();
        view.showLogo();
        view.hideBackButton();

        // setup components
        this.view.setupViewPager();
        this.view.setupTabLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Do nothing for now...
    }

    @Override
    public void onPageSelected(int position) {
        selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Do nothing for now...
    }

    public void selectTab(int position) {
        view.selectTab(position);
    }
}
