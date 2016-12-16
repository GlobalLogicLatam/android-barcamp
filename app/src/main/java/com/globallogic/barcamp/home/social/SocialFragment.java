package com.globallogic.barcamp.home.social;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.globallogic.barcamp.BaseFragment;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.utils.DimensUtils;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public class SocialFragment extends BaseFragment<SocialPresenter> implements SocialView {

    private ListView lvSocial;
    private SwipeRefreshLayout swipeContainer;
    private View tweetButton;

    private TweetTimelineListAdapter adapter;


    public static SocialFragment newInstance() {
        return new SocialFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_social;
    }

    @Override
    protected SocialPresenter buildPresenter() {
        return new SocialPresenter(this);
    }

    @Override
    protected void initView(View view) {
        lvSocial = (ListView) view.findViewById(R.id.lv_social);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        tweetButton = view.findViewById(R.id.tweet_button);
        DimensUtils.getInstance().setHeightProportion(tweetButton, 60, 682);

        // set listener to twitter button
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TweetComposer.Builder builder = new TweetComposer.Builder(getContext())
                        .text(getString(R.string.hashtag_barcamp));
                builder.show();
            }
        });

        // set listener to swipe refresh layout
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadTweets();
            }
        });

        // set listener to hide/show tweet button
        lvSocial.setOnScrollListener(new OnScrollObserver() {
            @Override
            public void onScrollUp() {
                tweetButton.animate().translationY(0);
            }

            @Override
            public void onScrollDown() {
                tweetButton.animate().translationY(300);
            }
        });
    }


    @Override
    public void loadTweets() {
        SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query("barcampmdq")
                .resultType(SearchTimeline.ResultType.RECENT)
                .build();

        adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(searchTimeline)
                .setViewStyle(R.style.tw__TweetLightStyle)
                .build();
    }

    @Override
    public void render() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                lvSocial.setAdapter(adapter);
            }
        });
    }

    public abstract class OnScrollObserver implements AbsListView.OnScrollListener {

        int last = 0;
        boolean control = true;

        public abstract void onScrollUp();

        public abstract void onScrollDown();

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int current, int visibles, int total) {
            if (current < last && !control) {
                onScrollUp();
                control = true;
            } else if (current > last && control) {
                onScrollDown();
                control = false;
            }

            last = current;
        }
    }

}
