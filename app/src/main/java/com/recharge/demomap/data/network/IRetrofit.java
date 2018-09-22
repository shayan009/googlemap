package com.recharge.demomap.data.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public interface IRetrofit {

/*
  String baseurl="http://111.93.169.90/team4/hotedin";

   // String baseurl="http://192.168.1.82/team4/hotedin";
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .connectTimeout(6000, TimeUnit.SECONDS)
            .readTimeout(600, TimeUnit.SECONDS)
            .writeTimeout(600, TimeUnit.SECONDS)
            .build();

    *//*Gson gson = new GsonBuilder()
            .setLenient()
            .create();*//*

    Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(baseurl+"/webservice/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    API SERVICE = RETROFIT.create(API.class);*/
}
