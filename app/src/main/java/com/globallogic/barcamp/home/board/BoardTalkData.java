package com.globallogic.barcamp.home.board;

import android.util.Log;

import com.globallogic.barcamp.BarcampApplication;
import com.globallogic.barcamp.BuildConfig;
import com.globallogic.barcamp.data.firebase.callback.FirebaseTalkDataCallback;
import com.globallogic.barcamp.domain.Board;
import com.globallogic.barcamp.domain.Break;
import com.globallogic.barcamp.domain.Config;
import com.globallogic.barcamp.domain.Talk;
import com.google.firebase.database.DatabaseError;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 11/21/2016
 */

public class BoardTalkData implements FirebaseTalkDataCallback<List<Talk>> {

    private static final String TAG = BoardTalkData.class.getSimpleName();
    private static final long LOCAL_UTC = 10800000; // 3 hours

    private final BoardTalkListener boardTalkListener;

    public interface BoardTalkListener {
        void onFinishEmpty();

        void onFinish(Board board);

        void onFinishFailure();
    }

    public BoardTalkData(BoardTalkListener listener) {
        this.boardTalkListener = listener;
    }

    @Override
    public void onDataChanged(List<Talk> talks) {
        Config configData = BarcampApplication.get().getConfigData();

        long eventDate = configData.getEventDate() * 1000;
        long serverDate = getServerDate(configData);
        boolean canShow = serverDate >= eventDate;
        if (!canShow) {
            boardTalkListener.onFinishEmpty();
        } else {
            // get event data from Config
            long startTime = configData.getStartTime();
            long endTime = configData.getEndTime();
            long interval = configData.getInterval();

            // add talks to board
            Board board = new Board();

            // add register time
            long registerTime = startTime - 3600; // 1 hour left for register time
            board.addRegister(registerTime);

            int groups = (int) ((endTime - startTime) / interval);
            for (int i = 0; i <= groups; i++) {
                if (isBreakTime(startTime)) {
                    board.addBreak(startTime);
                } else {
                    int talksAmountByTime = getTalksAmountByTime(talks, startTime);
                    board.addHour(startTime, talksAmountByTime);
                }

                // increment time
                startTime = startTime + interval;
            }

            boardTalkListener.onFinish(board);
        }
    }

    @Override
    public void onCancelled(DatabaseError error) {
        Log.w(TAG, "Error data callback: ", error.toException());
        boardTalkListener.onFinishFailure();
    }

    private boolean isBreakTime(long time) {
        List<Break> breaks = BarcampApplication.get().getConfigData().getBreaks();
        boolean isBreak = false;
        int pos = 0;
        while (pos < breaks.size() && !isBreak) {
            isBreak = breaks.get(pos).getTime() == time;
            pos++;
        }
        return isBreak;
    }

    private int getTalksAmountByTime(List<Talk> talks, long time) {
        int count = 0;
        for (Talk talk : talks) {
            if (talk.getDate().equals(time)) {
                count++;
            }
        }
        return count;
    }

    private long getServerDate(Config configData) {
        if ("release".equals(BuildConfig.BUILD_TYPE)) {
            return configData.getServerTime() - LOCAL_UTC;
        } else {
            return System.currentTimeMillis();
        }
    }
}
