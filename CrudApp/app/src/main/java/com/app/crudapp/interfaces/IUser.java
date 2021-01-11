package com.app.crudapp.interfaces;

import com.app.crudapp.models.UsuarioModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IUser {
    @GET("logins/")
    Call<List<UsuarioModel>> GetAll();

    @GET("logins/{id}/")
    Call<UsuarioModel> GetById(@Path("id") String id);

    @GET("logins/")
    Call<List<UsuarioModel>> CheckUser(@Query("User") String User, @Query("Password") String Password);

    @POST("logins/")
    Call<UsuarioModel> AddNew(@Body UsuarioModel usuarioModel);

    @PATCH("logins/{id}")
    Call<UsuarioModel> Edit(@Path("id") String id, @Body UsuarioModel usuarioModel);

    @DELETE("logins/{id}")
    Call<ResponseBody> Delete(@Path("id") String id);
}
