package com.elm.boycothelper.MVVMmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elm.boycothelper.model.ProductModel;
import com.elm.boycothelper.retrofit.ProductsService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsViewModel extends ViewModel {

    private final MutableLiveData<List<ProductModel>> productsList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private boolean isDataLoaded = false; // Added boolean flag

    public LiveData<List<ProductModel>> getProductsList() {
        return productsList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchProducts(ProductsService productsService) {
        if (!isDataLoaded) { // Check if data is not already loaded
            isLoading.setValue(true);

            Call<List<ProductModel>> call = productsService.getProducts();
            call.enqueue(new Callback<List<ProductModel>>() {
                @Override
                public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                    isLoading.setValue(false);
                    if (response.isSuccessful()) {
                        List<ProductModel> productModelList = response.body();
                        productsList.setValue(productModelList);
                        isDataLoaded = true; // Set the flag to true after data is loaded
                    } else {
                        // Handle error
                    }
                }

                @Override
                public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                    isLoading.setValue(false);
                    // Handle failure
                }
            });
        }
    }
}