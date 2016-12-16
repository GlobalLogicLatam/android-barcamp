package com.globallogic.barcamp.talk;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globallogic.barcamp.BarcampApplication;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.data.firebase.callback.GetDownloadUrlFileStorageCallback;
import com.globallogic.barcamp.data.repository.StorageDataRepository;
import com.globallogic.barcamp.domain.Talk;
import com.globallogic.barcamp.loader.ImageLoader;

import java.util.List;

/**
 * Created by Gonzalo.Martin on 10/12/2016
 */

public class TalkAdapter extends RecyclerView.Adapter<TalkAdapter.TalkViewHolder> {

    private final TalkCallback callback;
    private final boolean showEdit;
    private List<Talk> list;
    private Context context;

    public interface TalkCallback {
        void onTalkMenuSelected(View view, Talk talk);

        void onTalkImageSelected(String name, String url);
    }

    public TalkAdapter(Context context, List<Talk> list, boolean showEdit, TalkCallback callback) {
        this.list = list;
        this.showEdit = showEdit;
        this.callback = callback;
        this.context = context;
    }

    @Override
    public TalkViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item_talk, viewGroup, false);
        return new TalkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TalkViewHolder holder, int position) {
        final Talk talk = list.get(position);

        // set talk name
        holder.title.setText(talk.getName());

        // set room name
        holder.room.setText(talk.getRoomName());

        // set speaker name
        String speaker = talk.getSpeakerName();
        if (!talk.getTwitter().isEmpty()) {
            speaker = speaker + " (" + talk.getTwitter() + ")";
        }
        holder.speaker.setText(speaker);

        // set description
        if (talk.getDescription() != null && !talk.getDescription().isEmpty()) {
            holder.description.setText(context.getString(R.string.description_placeholder, talk.getDescription()));
        } else {
            holder.description.setText(R.string.description_empty);
        }

        // set edit button visibility
        holder.editButton.setVisibility(showEdit ? View.VISIBLE : View.GONE);

        // load image
        if (talk.getPhoto() != null && !talk.getPhoto().isEmpty()) {
            holder.talkImage.setVisibility(View.VISIBLE);
            loadImage(talk.getPhoto(), holder.talkImage);
        } else {
            holder.talkImage.setVisibility(View.GONE);
        }

        // check delayed state
        holder.delayedText.setVisibility(talk.isDelayed() ? View.VISIBLE : View.GONE);

        // set arrow icon visibility
        holder.arrowIcon.setVisibility(View.VISIBLE);

        // set card click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.toogleViewExpanded(talk.getPhoto() == null || talk.getPhoto().isEmpty());
            }
        });

        // set edit button listener
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onTalkMenuSelected(view, talk);
            }
        });

        // set card image listener
        holder.talkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onTalkImageSelected(talk.getName(), talk.getPhoto());
            }
        });
    }

    private void loadImage(final String url, final ImageView talkImage) {
        if (BarcampApplication.get().containsUrl(url)) {
            String finalUrl = BarcampApplication.get().getFinalUrl(url);
            ImageLoader.load(context, finalUrl, talkImage, R.drawable.ic_talk_image_placeholder);
        } else {
            StorageDataRepository storageDataRepository = new StorageDataRepository();
            storageDataRepository.getDownloadUrlFile(url, new GetDownloadUrlFileStorageCallback() {
                @Override
                public void onGetDownloadUrlFileSuccess(Uri uri) {
                    BarcampApplication.get().addImageUrl(url, uri.toString());
                    ImageLoader.load(context, uri.toString(), talkImage, R.drawable.ic_talk_image_placeholder);
                }

                @Override
                public void onGetDownloadUrlFileFailure(Throwable error) {
                    // Do nothing..
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TalkViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView room;
        TextView speaker;
        TextView description;
        View moreInfoView;
        ImageView arrowIcon;
        View editButton;
        ImageView talkImage;
        View delayedText;
        boolean expanded = false;

        public TalkViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.talk_title);
            room = (TextView) itemView.findViewById(R.id.talk_room);
            speaker = (TextView) itemView.findViewById(R.id.talk_speaker);
            description = (TextView) itemView.findViewById(R.id.talk_description);
            moreInfoView = itemView.findViewById(R.id.more_info_container);
            arrowIcon = (ImageView) itemView.findViewById(R.id.arrow_icon);
            editButton = itemView.findViewById(R.id.edit_button);
            talkImage = (ImageView) itemView.findViewById(R.id.talk_image);
            delayedText = itemView.findViewById(R.id.delayed_text);
        }

        public void toogleViewExpanded(boolean disablePhoto) {
            expanded = !expanded;
            moreInfoView.setVisibility(expanded ? View.VISIBLE : View.GONE);
            talkImage.setVisibility(disablePhoto ? View.GONE : View.VISIBLE);
            arrowIcon.setImageResource(expanded ? R.drawable.ic_arrow_up : R.drawable.ic_arrow);
        }
    }
}
