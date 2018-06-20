package com.omagrahari.hackernews.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TopStory extends RealmObject implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("by")
    private String userName;

    @SerializedName("descendants")
    private int totalCommentCount;

    @SerializedName("title")
    private String title;

    @SerializedName("score")
    private int score;

    @SerializedName("type")
    private String type;

    @SerializedName("url")
    private String url;

    @SerializedName("time")
    private long timeStamp;

    @SerializedName("kids")
    private RealmList<Long> commentIds;


    /************************************************
     * Getter Setter
     ************************************************/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalCommentCount() {
        return totalCommentCount;
    }

    public void setTotalCommentCount(int totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public RealmList<Long> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(RealmList<Long> commentIds) {
        this.commentIds = commentIds;
    }
}
