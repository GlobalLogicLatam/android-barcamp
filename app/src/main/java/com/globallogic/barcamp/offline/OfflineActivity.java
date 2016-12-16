package com.globallogic.barcamp.offline;

import android.view.View;
import android.widget.TextView;

import com.globallogic.barcamp.BaseActivity;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.utils.DeviceUtils;

/**
 * Created by Gonzalo.Martin on 10/28/2016
 */
public class OfflineActivity extends BaseActivity<OfflinePresenter> implements OfflineView {

    @Override
    protected OfflinePresenter buildPresenter() {
        return new OfflinePresenter(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_offline;
    }

    @Override
    protected int getRootLayoutId() {
        return R.id.root_offline;
    }

    @Override
    protected void setControls() {
        // Do nothing..
    }

    @Override
    protected void init() {
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.app_name);
        findViewById(R.id.toolbar_back_button).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (DeviceUtils.isNetworkAvailable(this)) {
            finish();
        }
    }
}
