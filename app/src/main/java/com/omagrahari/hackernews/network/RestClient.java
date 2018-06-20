package com.omagrahari.hackernews.network;

import com.omagrahari.hackernews.HackerNewsApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0/";

    private APIService apiService;
    private Retrofit retrofit;

    public RestClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(HackerNewsApplication.getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(APIService.class);
    }

    public APIService getApiService() {
        return apiService;
    }

    public void setApiService(APIService apiService) {
        this.apiService = apiService;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }
}
