package com.globallogic.barcamp.home.board;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globallogic.barcamp.R;
import com.globallogic.barcamp.domain.Board;
import com.globallogic.barcamp.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Gonzalo.Martin on 10/5/2016
 */

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private static final int BREAK_VIEW_TYPE = 0;
    private static final int HOUR_VIEW_TYPE = 1;
    private static final int REGISTER_VIEW_TYPE = 2;

    private final BoardCallback callback;

    public interface BoardCallback {
        void onItemSelected(Long talkTime);
    }

    private List<Map.Entry<Long, Integer>> list;

    public BoardAdapter(Board data, BoardCallback callback) {
        this.callback = callback;
        this.list = data.getEntryList();
    }

    @Override
    public BoardAdapter.BoardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        switch (viewType) {
            case REGISTER_VIEW_TYPE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item_board_info, viewGroup, false);
                return new RegisterBoardViewHolder(v);
            case BREAK_VIEW_TYPE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item_board_info, viewGroup, false);
                return new BreakBoardViewHolder(v);
            case HOUR_VIEW_TYPE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item_board, viewGroup, false);
                return new HoursBoardViewHolder(v);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Map.Entry<Long, Integer> entry = list.get(position);
        if (isBreak(entry.getValue())) {
            return BREAK_VIEW_TYPE;
        } else if (isRegister(entry.getValue())) {
            return REGISTER_VIEW_TYPE;
        } else {
            return HOUR_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(BoardAdapter.BoardViewHolder holder, int position) {
        final Map.Entry<Long, Integer> entry = list.get(position);
        holder.onBindView(entry);

        if (getItemViewType(position) == HOUR_VIEW_TYPE) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onItemSelected(entry.getKey());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private boolean isBreak(Integer value) {
        return Board.BREAK_TYPE.equals(value);
    }

    private boolean isRegister(Integer value) {
        return Board.REGISTER_TYPE.equals(value);
    }

    abstract static class BoardViewHolder extends RecyclerView.ViewHolder {

        TextView hour;
        TextView talks;

        public BoardViewHolder(View itemView) {
            super(itemView);
            hour = (TextView) itemView.findViewById(R.id.hour);
            talks = (TextView) itemView.findViewById(R.id.talks);
        }

        abstract void onBindView(Object object);

        public void setStyle(Context context, TextView textView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextAppearance(R.style.BoardTalkItemInfoText);
            } else {
                textView.setTextAppearance(context, R.style.BoardTalkItemInfoText);
            }
        }

        protected void setHourText(Long epochTime) {
            String formattedDate = StringUtils.formatEpochTimeToHumanReadable(epochTime);
            hour.setText(itemView.getContext().getString(R.string.board_item_hour, formattedDate));
        }
    }


    private static class BreakBoardViewHolder extends BoardViewHolder {

        public BreakBoardViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onBindView(Object object) {
            Map.Entry<Long, Integer> entry = (Map.Entry<Long, Integer>) object;
            final Long epochTime = entry.getKey();
            setHourText(epochTime);
            talks.setText(itemView.getContext().getString(R.string.break_item));
            setStyle(itemView.getContext(), hour);
            setStyle(itemView.getContext(), talks);
        }
    }

    private static class RegisterBoardViewHolder extends BreakBoardViewHolder {

        public RegisterBoardViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onBindView(Object object) {
            Map.Entry<Long, Integer> entry = (Map.Entry<Long, Integer>) object;
            final Long epochTime = entry.getKey();
            setHourText(epochTime);
            talks.setText(itemView.getContext().getString(R.string.register_item));
            setStyle(itemView.getContext(), hour);
            setStyle(itemView.getContext(), talks);
        }
    }

    private static class HoursBoardViewHolder extends BoardViewHolder {

        public HoursBoardViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onBindView(Object object) {
            Map.Entry<Long, Integer> entry = (Map.Entry<Long, Integer>) object;
            final Long epochTime = entry.getKey();
            setHourText(epochTime);
            Integer talksAmount = entry.getValue();
            if (talksAmount == 1) {
                talks.setText(itemView.getContext().getString(R.string.single_talk, String.valueOf(talksAmount)));
            } else {
                talks.setText(itemView.getContext().getString(R.string.multiple_talk, String.valueOf(talksAmount)));
            }
        }
    }

}
