package com.elm.boycothelper.MVVMmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
    private boolean isDataLoaded = false; // Added boolean flag
    private MutableLiveData<Boolean> isInternetAvailable = new MutableLiveData<>();


    public LiveData<List<CategoryModel>> getCategoryList() {
        return categoryList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<Boolean> getIsInternetAvailable() {
        return isInternetAvailable;
    }
    private void checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            isInternetAvailable.setValue(activeNetworkInfo != null && activeNetworkInfo.isConnected());
        }
    }

    public void fetchData(CategoryService categoryService ,Context context) {
        checkInternetConnection(context); // Check internet connection before making the API call
        if (isInternetAvailable.getValue() != null && isInternetAvailable.getValue() && !isDataLoaded) {
            if (!isDataLoaded) { // Check if data is not already loaded
                isLoading.setValue(true);
                categoryService.getCategories().enqueue(new Callback<List<CategoryModel>>() {
                    @Override
                    public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                        isLoading.setValue(false);
                        if (response.isSuccessful()) {
                            categoryList.setValue(response.body());
                            isDataLoaded = true; // Set the flag to true after data is loaded
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
    }
}
