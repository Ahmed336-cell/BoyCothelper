package com.elm.boycothelper.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elm.boycothelper.R;
import com.elm.boycothelper.adapter.CategoryAdapter;
import com.elm.boycothelper.adapter.ProductsAdapter;
import com.elm.boycothelper.model.CategoryModel;
import com.elm.boycothelper.model.ProductModel;
import com.elm.boycothelper.retrofit.CategoryService;
import com.elm.boycothelper.retrofit.ProductsService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProductsFragment extends Fragment {

    RecyclerView recyclerView;
    ProductsAdapter productsAdapter;
    ProgressBar progressBar;
    private static final String TAG = "MAIN_TAG";
    SearchView searchView;

    List<ProductModel> filteredProducts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_products, container, false);
        recyclerView = view.findViewById(R.id.productsRv);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fetchDataApi();
        return view;
    }

    private void filterList(String newText) {
        List<ProductModel>filterdList= new ArrayList<>();
        for (ProductModel item :filteredProducts){
            if (item.getName().toLowerCase().contains(newText.toLowerCase())){
                filterdList.add(item);
            }
        }
        if (filterdList.isEmpty()){
            Toast.makeText(getContext(), "No Data Founded", Toast.LENGTH_SHORT).show();
        }else{
            productsAdapter.setFilterd(filterdList);
        }
    }

    private void fetchDataApi() {
        progressBar.setVisibility(View.VISIBLE); // Show ProgressBar
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://productsupport.somee.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductsService productsService = retrofit.create(ProductsService.class);
        Call<List<ProductModel>> call = productsService.getProducts();
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar
                if (response.isSuccessful()) {
                    List<ProductModel> productModelList = response.body();

                    String ca = "";
                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        ca = bundle.getString("cate", "");
                    }
                    for (ProductModel product : productModelList) {
                        if (ca.equals(product.getDescription())) {
                            filteredProducts.add(product);
                        }
                        productsAdapter = new ProductsAdapter(getContext(), filteredProducts);
                        recyclerView.setAdapter(productsAdapter);
                    }
                }else {
                    Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Faild");
                }


            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG,t.getMessage());

            }
        });
    }

}