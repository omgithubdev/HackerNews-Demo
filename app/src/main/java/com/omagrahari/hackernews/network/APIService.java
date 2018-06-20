package com.omagrahari.hackernews.network;


import com.omagrahari.hackernews.model.Comment;
import com.omagrahari.hackernews.model.TopStory;
import com.omagrahari.hackernews.network.Response.ResponseTopStoryId;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    /**
     * Request to fetch Top Stories ID
     */
    @GET("topstories.json")
    Call<ResponseTopStoryId> fetchTopStoriesId(@Query("print") String query);

    /**
     * Using Model class as Realm doesnot support multilevel inheritance
     */

    /**
     * Request to Fetch A Story
     */
    @GET("item/{story_id}")
    Call<TopStory> fetchTopStory(@Path("story_id") String storyId, @Query("print") String query);

    /**
     * Request to Fetch A Comment
     */
    @GET("item/{comment_id}")
    Call<Comment> fetchComment(@Path("comment_id") String commentId, @Query("print") String query);
}
