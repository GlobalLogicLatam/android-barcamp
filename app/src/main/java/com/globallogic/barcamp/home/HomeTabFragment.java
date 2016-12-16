package com.globallogic.barcamp.home;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.globallogic.barcamp.BaseFragment;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.home.board.BoardFragment;
import com.globallogic.barcamp.home.social.SocialFragment;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public class HomeTabFragment extends BaseFragment<HomeTabPresenter> implements HomeTabView {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public static HomeTabFragment newInstance() {
        return new HomeTabFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home_tab;
    }

    @Override
    protected HomeTabPresenter buildPresenter() {
        return new HomeTabPresenter(this);
    }

    @Override
    public void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.vpPager);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
    }

    @Override
    public void setupViewPager() {
        viewPager.setAdapter(new HomePagerAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(presenter);
    }

    @Override
    public void setupTabLayout() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.getTabAt(0).setText(R.string.board);
        tabLayout.getTabAt(1).setText(R.string.hashtag_barcamp);
    }

    @Override
    public void showLogo() {
        ((MainActivity) getActivity()).displayToolbarLogo();
    }

    @Override
    public void setTitle() {
        setToolbarTitle(getString(R.string.app_name));
    }

    @Override
    public void selectTab(int position) {
        viewPager.setCurrentItem(position);
    }

    private static class HomePagerAdapter extends FragmentPagerAdapter {
        private static int ITEMS = 2;

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BoardFragment.newInstance();
                case 1:
                    return SocialFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return ITEMS;
        }
    }
}
