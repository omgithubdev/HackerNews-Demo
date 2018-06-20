package com.omagrahari.hackernews.activity.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.omagrahari.hackernews.R;
import com.omagrahari.hackernews.adapter.ViewPagerAdapter;
import com.omagrahari.hackernews.fragment.ArticleFragment;
import com.omagrahari.hackernews.fragment.CommentFragment;
import com.omagrahari.hackernews.helper.CoreGsonUtils;
import com.omagrahari.hackernews.helper.DatabaseHelper;
import com.omagrahari.hackernews.helper.DateHelper;
import com.omagrahari.hackernews.model.TopStory;

import java.util.ArrayList;

public class StoryDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TopStory topStory;
    private TabLayout tabLayout;
    private ViewPager pager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView titleText, urlText, usernameText, timestampText;
    private boolean isFetchedFromCache = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);


        //toolbar settings
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        isFetchedFromCache = getIntent().getExtras().getBoolean("FETCHED_FROM_CACHE");
        if (isFetchedFromCache) {
            //fetch the data from database
            long topStoryId = getIntent().getExtras().getLong("TOP_STORY_ID");
            topStory = DatabaseHelper.getTopStory(topStoryId);
        } else {
            //take the data passed via intent
            String strTopStory = getIntent().getExtras().getString("TOP_STORY");
            Gson gson = new Gson();
            topStory = gson.fromJson(strTopStory, TopStory.class);
        }
        //initilaize views
        setViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setViews() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_layout);
        titleText = (TextView) findViewById(R.id.title_text);
        urlText = (TextView) findViewById(R.id.url);
        usernameText = (TextView) findViewById(R.id.username);
        timestampText = (TextView) findViewById(R.id.time_stamp);


        //setup viewpager
        setupViewPagerAndTabs();

        collapsingToolbarLayout.setTitle(topStory.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.fui_transparent)); // transperent color = #00000000
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255));

        //set values
        titleText.setText(topStory.getTitle());
        if (topStory.getUrl() != null) {
            urlText.setText(topStory.getUrl());
        } else {
            urlText.setText("");
        }
        usernameText.setText(topStory.getUserName());
        timestampText.setText(DateHelper.parseDate(String.valueOf(topStory.getTimeStamp())));
    }


    public void setupViewPagerAndTabs() {

        ArrayList<String> listTitle = new ArrayList<>();
        listTitle.add(String.valueOf(topStory.getTotalCommentCount()) + "  COMMENTS");
        listTitle.add("ARTICLE");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), this, listTitle);

        //create first fragment
        CommentFragment commentFragment = new CommentFragment();
        //passsing data
        Bundle args1 = new Bundle();
        if (topStory.getCommentIds() != null) {
            if (topStory.getCommentIds().size() > 0) {
                args1.putBoolean("COMMENT_EXIST", true);
                String commentString = CoreGsonUtils.toJson(topStory.getCommentIds());
                args1.putCharSequence("COMMENT_IDS", commentString);
            } else {
                args1.putBoolean("COMMENT_EXIST", false);
            }
        } else {
            args1.putBoolean("COMMENT_EXIST", false);
        }
        commentFragment.setArguments(args1);
        adapter.addFragment(commentFragment);


        //create Second frgament
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle args = new Bundle();
        if (topStory.getUrl() != null) {
            if (!topStory.getUrl().isEmpty()) {
                args.putCharSequence("URL", topStory.getUrl());
                args.putBoolean("URL_EXIST", true);
            } else {
                args.putBoolean("URL_EXIST", false);
            }
        } else {
            args.putBoolean("URL_EXIST", false);
        }
        articleFragment.setArguments(args);
        adapter.addFragment(articleFragment);

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.setCurrentItem(0);
        pager.setOffscreenPageLimit(2);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab newTab = tabLayout.getTabAt(i);
            newTab.setCustomView(adapter.getTabView(i));
        }

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
