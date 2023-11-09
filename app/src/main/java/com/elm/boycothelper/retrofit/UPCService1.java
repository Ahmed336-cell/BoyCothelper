package com.elm.boycothelper.retrofit;

import com.elm.boycothelper.model.ProductsJson1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UPCService1 {

    @GET("product/{upc}")
    Call<ProductsJson1> getProductData(
            @Path("upc") String barcode,
            @Query("apikey") String apiKey
    );



}

