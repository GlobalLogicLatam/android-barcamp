package com.globallogic.barcamp.home;

import com.globallogic.barcamp.BaseView;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public interface HomeTabView extends BaseView {

    void setupViewPager();

    void selectTab(int position);

    void setupTabLayout();

    void setTitle();

    void hideBackButton();

    void showLogo();
}
