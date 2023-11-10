package com.elm.boycothelper.MVVMmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elm.boycothelper.model.CategoryModel;
import com.elm.boycothelper.retrofit.CategoryService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {

    private MutableLiveData<List<CategoryModel>> categoryList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


    public LiveData<List<CategoryModel>> getCategoryList() {
        return categoryList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchData(CategoryService categoryService) {
        isLoading.setValue(true);
        categoryService.getCategories().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    categoryList.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                isLoading.setValue(false);
                // Handle failure
            }
        });
    }
}
