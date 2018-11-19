package com.example.bstage.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit instances;

    public static Retrofit getInstances(){

        if(instances==null)
            instances = new Retrofit.Builder()
                    .baseUrl("https://backstage-backend.herokuapp.com")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instances;
    }
}
