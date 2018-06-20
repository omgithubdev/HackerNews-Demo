package com.omagrahari.hackernews;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.omagrahari.hackernews.network.APIService;
import com.omagrahari.hackernews.network.RestClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class HackerNewsApplication extends Application {

    private static final int READ_TIMEOUT = 60 * 1000;
    private static final int CONNECTION_TIMEOUT = 60 * 1000;
    private static RestClient restClient;
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;
    private static Context mContext;
    private static Realm realm;
    private static RealmConfiguration realmConfiguration;
    private FirebaseAuth mAuth;
    /**
     * RestClient Instance
     *
     * @return
     */
    private static RestClient getRestClient() {
        if (restClient == null) {
            restClient = new RestClient();
            retrofit = restClient.getRetrofit();
        }
        return restClient;
    }

    /**
     * Get Realm Instance
     *
     * @return
     */
    public static Realm getRealm() {
        if (realm == null) {
            realm = Realm.getInstance(getRealmConfiguration());
            return realm;
        }
        return realm;
    }

    public static void setRealm(Realm realm) {
        HackerNewsApplication.realm = realm;
    }

    public static RealmConfiguration getRealmConfiguration() {
        realmConfiguration = new RealmConfiguration.Builder().name("urbanpiper.realm")
                .deleteRealmIfMigrationNeeded().schemaVersion(1).build();
        return realmConfiguration;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mContext = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
         /*Local database*/
        Realm.init(this);
        getRealm();

    }

    /**
     * OkHttp Instance
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
            logger.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            okHttpClient = new OkHttpClient.Builder().cache(getCache(4)).retryOnConnectionFailure(true).readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .addInterceptor(logger)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {

                            okhttp3.Request request;
                            request = chain.request().newBuilder()
                                    .addHeader("Accept", "application/json")
                                    .addHeader("Content-type", "application/json")
                                    .build();

                            return chain.proceed(request);
                        }
                    }).build();

        }
        return okHttpClient;
    }

    private static okhttp3.Cache getCache(int size_in_mb) {
        int cacheSize = size_in_mb * 1024 * 1024; //10MB
        okhttp3.Cache cache = new okhttp3.Cache(mContext.getCacheDir(), cacheSize);
        return cache;
    }

    public static APIService getAPIService() {
        return getRestClient().getApiService();
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        HackerNewsApplication.retrofit = retrofit;
    }
}
