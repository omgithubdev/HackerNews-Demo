package com.omagrahari.hackernews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omagrahari.hackernews.HackerNewsApplication;
import com.omagrahari.hackernews.R;
import com.omagrahari.hackernews.activity.home.StoryDetailsActivity;
import com.omagrahari.hackernews.helper.CoreGsonUtils;
import com.omagrahari.hackernews.helper.DateHelper;
import com.omagrahari.hackernews.model.TopStory;

import java.util.ArrayList;
import java.util.List;


public class TopStoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<TopStory> topStories = new ArrayList<>();
    private boolean isFetchedFromCache = false;

    public ArrayList<TopStory> getTopStories() {
        return topStories;
    }

    public void setTopStories(ArrayList<TopStory> topStories) {
        this.topStories = topStories;
    }

    public boolean isFetchedFromCache() {
        return isFetchedFromCache;
    }

    public void setFetchedFromCache(boolean fetchedFromCache) {
        isFetchedFromCache = fetchedFromCache;
    }

    /**
     * Constructor
     *
     * @param context
     */
    public TopStoryAdapter(Context context, List<TopStory> topStories) {
        this.context = context;
        this.topStories.addAll(topStories);
    }

    /**
     * Update data
     *
     * @param topStories
     */
    public void setData(List<TopStory> topStories) {
        topStories.clear();
        this.topStories.addAll(topStories);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.card_top_stories, parent, false);
        return new TopStoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TopStoryViewHolder topStoryViewHolder = (TopStoryViewHolder) holder;
        topStoryViewHolder.setViews(topStories.get(position));
    }

    @Override
    public int getItemCount() {
        return topStories.size();
    }

    private class TopStoryViewHolder extends RecyclerView.ViewHolder {

        private TextView title, score, url, username, timeStamp, commentCount;
        View itemView;

        /**
         * Constructor
         *
         * @param itemView
         */
        public TopStoryViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            score = (TextView) itemView.findViewById(R.id.score);
            url = (TextView) itemView.findViewById(R.id.url);
            username = (TextView) itemView.findViewById(R.id.username);
            timeStamp = (TextView) itemView.findViewById(R.id.time_stamp);
            commentCount = (TextView) itemView.findViewById(R.id.comment_count);
        }

        /**
         * Set Data to views
         *
         * @param topStory
         */
        public void setViews(final TopStory topStory) {
            if (topStory != null) {
                try {
                    if (topStory.getTitle() != null) {
                        title.setText(topStory.getTitle());
                    }
                    score.setText(String.valueOf(topStory.getScore()));
                    if (url != null) {
                        url.setText(topStory.getUrl());
                    } else {
                        url.setVisibility(View.GONE);
                    }
                    if (topStory.getUserName() != null) {
                        username.setText(topStory.getUserName());
                    } else {
                        username.setVisibility(View.INVISIBLE);
                    }

                    commentCount.setText(String.valueOf(topStory.getTotalCommentCount()));

                    timeStamp.setText(DateHelper.parseDate(String.valueOf(topStory.getTimeStamp())));

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            navigateToStoryDetailsActivity(topStory);
                        }
                    });

                } catch (NullPointerException npe) {

                }
            }
        }

        /**
         * Navigate to Details Activity
         */
        public void navigateToStoryDetailsActivity(TopStory topStory) {

            Intent intent = new Intent(HackerNewsApplication.getAppContext(), StoryDetailsActivity.class);
            if (isFetchedFromCache()) {
                //when fetched from cache pass the primary key of the object and fetch the objec from db
                intent.putExtra("FETCHED_FROM_CACHE", true);
                intent.putExtra("TOP_STORY_ID", topStory.getId());

            } else {
                //when fetched from server; pass the object to other activity
                String strTopStory = CoreGsonUtils.toJson(topStory);
                intent.putExtra("TOP_STORY", strTopStory);
                intent.putExtra("FETCHED_FROM_CACHE", false);
            }
            context.startActivity(intent);
        }
    }
}

