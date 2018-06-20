package com.omagrahari.hackernews.helper;

import com.omagrahari.hackernews.HackerNewsApplication;
import com.omagrahari.hackernews.model.Comment;
import com.omagrahari.hackernews.model.TopStory;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class DatabaseHelper {

    /**
     * Add All Top Stories
     */
    public static void addTopStories(final List<TopStory> topStories) {

        Realm realm = HackerNewsApplication.getRealm();
        realm.executeTransactionAsync
                (new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(topStories);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        //TODO

                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        //TODO
                    }
                });
    }

    /**
     * Get All Top stories
     */
    public static ArrayList<TopStory> getTopStories() {
        ArrayList<TopStory> topStories = new ArrayList<>();

        RealmResults<TopStory> realmResults = HackerNewsApplication.getRealm().where(TopStory.class).equalTo("type", "story").greaterThan("id", 0).findAll();
        if (realmResults.isLoaded()) {
            if (realmResults.size() > 0) {
                topStories.addAll(realmResults.subList(0, realmResults.size()));
            }
        }
        return topStories;
    }

    /**
     * Get a Top Story for the ID
     */
    public static TopStory getTopStory(long id) {
        TopStory topStory = null;
        try {
            Realm realm = HackerNewsApplication.getRealm();
            realm.beginTransaction();
            topStory = realm.where(TopStory.class).equalTo("id", id).equalTo("type", "story").findFirst();
            realm.commitTransaction();

        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return topStory;
    }

    /**
     * Get the Length of Top Storeis saved
     */
    public static int getSizeTopStories() {
        int size = 0;
        try {
            size = HackerNewsApplication.getRealm().where(TopStory.class).equalTo("type", "story").greaterThan("id", 0).findAll().size();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * Get ALL Comment for the ids
     */
    public static ArrayList<Comment> getCommentForIds(RealmList<Long> commentIds, int maxItem) {
        ArrayList<Comment> comments = new ArrayList<>();
        Long arr[] = new Long[maxItem];
        for (int i = 0; i < maxItem; i++) {
            arr[i] = commentIds.get(i);
        }

        //make the query
        RealmQuery<Comment> realmQuery = HackerNewsApplication.getRealm().where(Comment.class);
        realmQuery.in("id", arr);
        RealmResults<Comment> realmResults = realmQuery.findAll();
        if (realmResults.isLoaded()) {
            if (realmResults.size() > 0) {
                comments.addAll(realmResults.subList(0, realmResults.size()));
            }
        }
        return comments;
    }

    /**
     * Add All Commnets
     */
    public static void addAllComments(final List<Comment> comments) {
        Realm realm = HackerNewsApplication.getRealm();
        realm.executeTransactionAsync
                (new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(comments);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        //TODO

                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                    }
                });
    }


    /**
     * Clear All Cache Data
     */
    public static void clearTopStories() {
        Realm realm = HackerNewsApplication.getRealm();
        realm.executeTransactionAsync
                (new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //delete all top stories
                        RealmResults<TopStory> realmResults = realm.where(TopStory.class).equalTo("type", "story").findAll();
                        if (realmResults.size() > 0) {
                            realmResults.deleteAllFromRealm();
                        }

                        //delete all comments
                        RealmResults<Comment> realmResults1 = realm.where(Comment.class).equalTo("type", "comment").findAll();
                        if (realmResults1.size() > 0) {
                            realmResults1.deleteAllFromRealm();
                        }
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        //TODO
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        //TODO
                    }
                });
    }
}
