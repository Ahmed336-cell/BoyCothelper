package com.elm.boycothelper.retrofit;

import com.elm.boycothelper.model.CategoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("api/Category")
    Call<List<CategoryModel>> getCategories();

}
