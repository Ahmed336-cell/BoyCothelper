package com.elm.boycothelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.elm.boycothelper.MVVMmodel.ProductsViewModel;
import com.elm.boycothelper.R;
import com.elm.boycothelper.adapter.ProductsAdapter;
import com.elm.boycothelper.model.ProductModel;
import com.elm.boycothelper.pojo.Constants;
import com.elm.boycothelper.retrofit.ProductsService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductsFragment extends Fragment {

    private ProductsViewModel productsViewModel;
    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;

    private List<ProductModel> filteredProducts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

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

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        // Initialize ViewModel
        productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        // Make the API call in onCreate
        productsViewModel.fetchProducts(createProductsService());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeViewModel();
    }

    private void observeViewModel() {
        productsViewModel.getProductsList().observe(getViewLifecycleOwner(), this::updateProductsList);
        productsViewModel.getIsLoading().observe(getViewLifecycleOwner(), this::updateLoadingState);
    }

    private void updateProductsList(List<ProductModel> productsList) {
        if (productsList != null) {
            filteredProducts.clear();
            String ca = "";
            Bundle bundle = getArguments();
            if (bundle != null) {
                ca = bundle.getString(Constants.CATEGORY_KEY, "");
            }
            for (ProductModel product : productsList) {
                if (ca.equals(product.getDescription())) {
                    filteredProducts.add(product);
                }
            }
            productsAdapter = new ProductsAdapter(getContext(), filteredProducts);
            recyclerView.setAdapter(productsAdapter);
        } else {
            // Handle null or empty list
        }
    }

    private void updateLoadingState(Boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void filterList(String newText) {
        List<ProductModel> filteredList = new ArrayList<>();
        for (ProductModel item : filteredProducts) {
            if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.nodata), Toast.LENGTH_SHORT).show();
        } else {
            productsAdapter.setFilterd(filteredList);
        }
    }

    private ProductsService createProductsService() {
        return new Retrofit.Builder()
                .baseUrl(Constants.PRODUCT_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductsService.class);
    }
}
