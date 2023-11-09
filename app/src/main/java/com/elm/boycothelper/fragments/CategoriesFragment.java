package com.elm.boycothelper.fragments;

import android.os.Bundle;

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
import com.elm.boycothelper.model.CategoryModel;
import com.elm.boycothelper.retrofit.CategoryService;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriesFragment extends Fragment {

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    ProgressBar progressBar;
    private static final String TAG = "MAIN_TAG";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = view.findViewById(R.id.categoryRV);
        progressBar = view.findViewById(R.id.progressBarCat);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fetchDataApi();
        return view;
    }

    private void fetchDataApi(){
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://productsupport.somee.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CategoryService categoryService = retrofit.create(CategoryService.class);
        Call<List<CategoryModel>>call = categoryService.getCategories();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    List<CategoryModel>categoryModelList= response.body();
                    categoryAdapter = new CategoryAdapter(getContext(),categoryModelList);
                    recyclerView.setAdapter(categoryAdapter);
                    Log.d(TAG, String.valueOf(categoryModelList.size()));

                }else {
                    Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Faild");

                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG,t.getMessage());
            }
        });
    }
}