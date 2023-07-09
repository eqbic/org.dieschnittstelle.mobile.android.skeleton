package org.dieschnittstelle.mobile.android.skeleton.util;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.dieschnittstelle.mobile.android.skeleton.Model.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public class Authenticator {

    public static interface AuthService{
        @PUT("/api/users/auth")
        public Call<Boolean> authenticate(@Body User user);
    }

    private AuthService authService;

    public Authenticator(){
        Gson gson = new GsonBuilder().create();
        Retrofit webApiBase = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.authService = webApiBase.create(AuthService.class);
    }

    public boolean validate(User user){
        try{
            return this.authService.authenticate(user).execute().body();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
