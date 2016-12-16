package com.globallogic.barcamp.home;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.globallogic.barcamp.BaseActivity;
import com.globallogic.barcamp.BaseFragment;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.domain.Talk;
import com.globallogic.barcamp.talk.AddTalkFragment;
import com.globallogic.barcamp.talk.EditTalkFragment;
import com.globallogic.barcamp.talk.TalkListFragment;
import com.globallogic.barcamp.talk.image.TalkImageFragment;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    private TextView toolbarTitle;
    private View toolbarLogo;
    private View toolbarBackButton;

    @Override
    protected MainPresenter buildPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected int getRootLayoutId() {
        return R.id.root_main;
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (currentFragment instanceof HomeTabFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void setControls() {
        // set toolbar title
        toolbarTitle.setText(R.string.app_name);
    }

    @Override
    protected void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarBackButton = findViewById(R.id.toolbar_back_button);
        toolbarLogo = findViewById(R.id.toolbar_logo);

        // set toolbar as action bar
        setSupportActionBar(toolbar);

        // hide soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void setHomeTabsFragment() {
        setContentFragment(HomeTabFragment.newInstance());
    }

    @Override
    public void displayLogoutConfirmation(DialogInterface.OnClickListener positiveButton) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("La sesión de usuario se cerrará")
                .setPositiveButton("Confirmar", positiveButton)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();

        alertDialog.show();
    }

    @Override
    public void restartApp() {
        restart();
    }

    @Override
    public void notifyLogout() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (fragment != null && fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).notifyLogout();
        }
    }

    @Override
    public boolean fragmentCanBack() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.main_content);
        return fragment.canBack();
    }

    public void setTalkListFragment(Long talkTime) {
        setContentFragment(TalkListFragment.newInstance(talkTime));
    }

    public void setToolbarTitle(String text) {
        toolbarTitle.setText(text);
    }

    public void displayToolbarBackButton() {
        toolbarBackButton.setVisibility(View.VISIBLE);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void hideToolbarBackButton() {
        toolbarBackButton.setVisibility(View.GONE);
    }

    public void displayToolbarLogo() {
        toolbarLogo.setVisibility(View.VISIBLE);
    }

    public void hideToolbarLogo() {
        toolbarLogo.setVisibility(View.GONE);
    }

    public void setAddTalkFragment(Long talkEpochTime) {
        setContentFragment(AddTalkFragment.newInstance(talkEpochTime));
    }

    public void setEditTalkFragment(Talk talk) {
        setContentFragment(EditTalkFragment.newInstance(talk));
    }

    private void setContentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            presenter.logoutItemPressed();
            return true;
        }
        return false;
    }

    public void setTalkImageFragment(String name, String imageUrl) {
        setContentFragment(TalkImageFragment.newInstance(name, imageUrl));
    }
}
