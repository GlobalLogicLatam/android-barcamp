package com.globallogic.barcamp.home.board;

import android.os.Handler;
import android.util.Log;

import com.globallogic.barcamp.BarcampApplication;
import com.globallogic.barcamp.BasePresenter;
import com.globallogic.barcamp.data.firebase.FirebaseWorker;
import com.globallogic.barcamp.data.firebase.callback.ConfigDataCallback;
import com.globallogic.barcamp.domain.Board;
import com.globallogic.barcamp.domain.Config;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public class BoardPresenter extends BasePresenter<BoardView> implements BoardAdapter.BoardCallback {

    private static final long GET_CONFIG_TIMEOUT = 10000;

    private FirebaseWorker firebaseWorker = new FirebaseWorker();
    private boolean getConfigSuccess = false;

    private BoardTalkData boardTalkData;

    public BoardPresenter(final BoardView view) {
        super(view);
        boardTalkData = new BoardTalkData(new BoardTalkData.BoardTalkListener() {
            @Override
            public void onFinishEmpty() {
                view.renderEmpty();
            }

            @Override
            public void onFinish(Board board) {
                // dismiss loading
                view.dismissLoading();

                // update UI
                view.render(board);
            }

            @Override
            public void onFinishFailure() {
                view.dismissLoading();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseWorker.removeTalkEventListener();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        view.showLoading();

        getConfig();

        // in some point, Firebase executes logout of the current account, and we need to check that state
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!getConfigSuccess) {
                    // something is wrong with authentication
                    Log.w(TAG, "Firebase - Logout in progress");
                    firebaseWorker.logout();
                    firebaseWorker.removeTalkEventListener();
                    view.forceRestartApp();
                }
            }
        }, GET_CONFIG_TIMEOUT);
    }

    @Override
    public void onItemSelected(Long talkTime) {
        view.onItemSelected(talkTime);
    }

    @Override
    public void onLogout() {
        super.onLogout();
        firebaseWorker.removeTalkEventListener();
    }

    private void getBoard() {
        firebaseWorker.addTalkEventListener(boardTalkData);
    }

    /**
     * Retrieves the configuration node from backend
     */
    private void getConfig() {
        firebaseWorker.getConfig(new ConfigDataCallback() {
            @Override
            public void onConfigDataSuccess(Config config) {
                getConfigSuccess = true;
                BarcampApplication.get().setConfig(config);
                view.dismissLoading();
                getBoard();
            }

            @Override
            public void onConfigDataFailure(Throwable error) {
                view.dismissLoading();
                getBoard();
            }
        });
    }
}
