package com.bet.update.api;

import com.bet.update.ServiceGenerator;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private String url;

    public APIClient(String baseUrl) {//BuildConfig.API_URL
        this.url = baseUrl;
    }

    public Retrofit getClient(){
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(new ServiceGenerator().GenerateUnsafeOkHttpClient())
            .build();

        return retrofit;
    }
}
