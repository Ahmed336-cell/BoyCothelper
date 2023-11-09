package com.elm.boycothelper.retrofit;

import com.elm.boycothelper.model.ProductModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductsService {
    @GET("api/Product")
    Call<List<ProductModel>> getProducts();
}
