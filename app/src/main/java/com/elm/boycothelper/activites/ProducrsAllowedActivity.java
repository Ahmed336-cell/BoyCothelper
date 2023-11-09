package com.elm.boycothelper.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.elm.boycothelper.R;
import com.elm.boycothelper.fragments.CategoriesFragment;

public class ProducrsAllowedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producrs_allowed);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        fragmentTransaction.add(R.id.container, categoriesFragment);
        fragmentTransaction.commit();
    }
}