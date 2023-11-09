package com.elm.boycothelper.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.elm.boycothelper.R;
import com.elm.boycothelper.adapter.HomeRvAdapter;
import com.elm.boycothelper.model.HomeModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private HomeModel homeModel;
    private HomeRvAdapter homeRvAdapter;
    List<HomeModel> itemList = new ArrayList<>();

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.rvHome);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeRvAdapter = new HomeRvAdapter(this,itemList);
        recyclerView.setAdapter(homeRvAdapter);
        homeModel = new HomeModel(getString(R.string.product_checker),R.drawable.checll);
        itemList.add(homeModel);
        itemList.add( new HomeModel(getString(R.string.Products_Can_Buy),R.drawable.fi));
        homeRvAdapter.notifyItemInserted(itemList.size() - 1);
    }




}