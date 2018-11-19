package com.example.bstage.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {

    @POST("register")
    @FormUrlEncoded
    Observable <String> registerUser(@Field("Correo") String Correo,
                                             @Field("Name") String Name,
                                             @Field("Clave") String Clave);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("Correo") String Correo,
                                    @Field("Clave") String Clave);
}
