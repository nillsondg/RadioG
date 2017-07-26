package ru.guu.radiog.network;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dmitry on 22.06.17.
 */

public class ApiFactory {

    private static final String EVENDATE_HOST_NAME = "https://evendate.io";
    private static final String GUU_HOST_NAME = "https://my.guu.ru";
    private static final String ITUNES_HOST_NAME = "https://itunes.apple.com";
    private static final String LASTFM_HOST_NAME = "http://ws.audioscrobbler.com/";
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;
    private static final OkHttpClient CLIENT;


    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        CLIENT = httpClient.build();
    }

    @NonNull
    public static ApiService getEvendateService() {
        return getRetrofit(EVENDATE_HOST_NAME).create(ApiService.class);
    }

    @NonNull
    public static GuuApiService getGuuService() {
        return getRetrofit(GUU_HOST_NAME).create(GuuApiService.class);
    }

    @NonNull
    public static ItunesApiService getItunesService() {
        return getRetrofit(ITUNES_HOST_NAME).create(ItunesApiService.class);
    }

    @NonNull
    public static LastFmApiService getLastFmService() {
        return getRetrofit(LASTFM_HOST_NAME).create(LastFmApiService.class);
    }

    @NonNull
    private static Retrofit getRetrofit(String hostName) {
        return new Retrofit.Builder().baseUrl(hostName)
                .addConverterFactory(GsonConverterFactory.create()
                ).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(CLIENT).build();
    }
}
