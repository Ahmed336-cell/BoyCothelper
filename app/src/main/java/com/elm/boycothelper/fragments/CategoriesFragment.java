package com.elm.boycothelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elm.boycothelper.MVVMmodel.CategoryViewModel;
import com.elm.boycothelper.R;
import com.elm.boycothelper.adapter.CategoryAdapter;
import com.elm.boycothelper.model.CategoryModel;
import com.elm.boycothelper.retrofit.CategoryService;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriesFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = view.findViewById(R.id.categoryRV);
        progressBar = view.findViewById(R.id.progressBarCat);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Make the API call in onCreate
        categoryViewModel.fetchData(createCategoryService());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeViewModel();
    }

    private void observeViewModel() {
        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), this::updateCategoryList);
        categoryViewModel.getIsLoading().observe(getViewLifecycleOwner(), this::updateLoadingState);
    }

    private void updateCategoryList(List<CategoryModel> categoryList) {
        if (categoryList != null) {
            categoryAdapter = new CategoryAdapter(getContext(), categoryList);
            recyclerView.setAdapter(categoryAdapter);
        } else {
            // Handle null or empty list
        }
    }

    private void updateLoadingState(Boolean isLoading) {
        if (isLoading != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    private CategoryService createCategoryService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://productsupport.somee.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(CategoryService.class);
    }
}
