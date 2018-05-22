package com.lucasurbas.counter.remote.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lucasurbas.counter.remote.RemotePostsService;
import com.lucasurbas.counter.remote.model.AutoValueGsonFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RemoteModule {

    private static final String BASE_URL = "";

    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(AutoValueGsonFactory.create())
                .create();
    }

    @Provides
    OkHttpClient ProvideOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    RemotePostsService provideRemotePostsService(Retrofit retrofit) {
        return retrofit.create(RemotePostsService.class);
    }
}
