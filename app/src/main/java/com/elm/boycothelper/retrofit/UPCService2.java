package com.elm.boycothelper.retrofit;

import com.elm.boycothelper.model.ProductJson2;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface UPCService2 {
    @Headers("X-RapidAPI-Key: 28c925ba8bmshdfe86b84eea7cd5p17646djsnebc89f36b009")
    Call<ProductJson2> getItems2(@Query("code")String upcode);
}
