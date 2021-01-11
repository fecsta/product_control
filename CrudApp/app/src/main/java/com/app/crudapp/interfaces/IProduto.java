package com.app.crudapp.interfaces;

import com.app.crudapp.models.ProdutoModel;
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

public interface IProduto {
    @GET("produtos/")
    Call<List<ProdutoModel>> GetAll(@Query("UserID") int UserID);

    @POST("produtos/")
    Call<ProdutoModel> AddNew(@Body ProdutoModel produtoModel);

    @PATCH("produtos/{id}")
    Call<ProdutoModel> Edit(@Path("id") String id, @Body ProdutoModel produtoModel);

    @DELETE("produtos/{id}")
    Call<ResponseBody> Delete(@Path("id") String id);
}
