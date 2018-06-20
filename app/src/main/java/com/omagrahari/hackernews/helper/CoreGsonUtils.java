package com.omagrahari.hackernews.helper;

import com.google.gson.Gson;

import org.json.JSONArray;

import io.realm.RealmList;


public class CoreGsonUtils {

    private static Gson instance;

    private static Gson getGsonObject() {

        if (instance == null) {
            instance = new Gson();
        }
        return instance;

    }

    public static <T> String toJson(Object obj) {

        String gsonstr = "";

        Gson gson = getGsonObject();
        gsonstr = gson.toJson(obj);
        return gsonstr;
    }


    public static <T extends Long> RealmList<T> fromJsontoRealmList(String string, Class<T> model) {

        Gson gson = getGsonObject();
        T gfromat = null;
        RealmList<T> localArrayList = new RealmList<>();
        try {
            JSONArray jsonInner = new JSONArray(string);

            int i = 0;
            while (i < jsonInner.length()) {
                gfromat = gson.fromJson(jsonInner.get(i).toString(), model);
                localArrayList.add(gfromat);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return localArrayList;
    }
}
