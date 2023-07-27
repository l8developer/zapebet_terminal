package com.bet.update.api;

import com.bet.update.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClientGson {

    private String url;

    public APIClientGson(String baseUrl) {//BuildConfig.API_URL
        this.url = baseUrl;
    }

    public Retrofit getClient(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(new ServiceGenerator().GenerateUnsafeOkHttpClient())
            .build();

        return retrofit;
    }
}
