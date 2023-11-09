package com.elm.boycothelper.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.elm.boycothelper.R;
import com.elm.boycothelper.fragments.CategoriesFragment;
import com.elm.boycothelper.fragments.ProductsFragment;
import com.elm.boycothelper.model.CategoryModel;
import com.elm.boycothelper.model.HomeModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    private List<CategoryModel> categoryModelList;

    public CategoryAdapter(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_display_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel categoryModel = categoryModelList.get(position);
        holder.name.setText(categoryModel.getName());
        if (categoryModel.getName().equals("Cheese")){
            holder.imageView.setImageResource(R.drawable.cheese);
        } else if (categoryModel.getName().equals("Chips")) {
            holder.imageView.setImageResource(R.drawable.chips);
        }else if (categoryModel.getName().equals("Chocolate")) {
            holder.imageView.setImageResource(R.drawable.chocolate);
        }else if (categoryModel.getName().equals("Cafe")) {
            holder.imageView.setImageResource(R.drawable.coffee);
        }else if (categoryModel.getName().equals("Cosmetic")) {
            holder.imageView.setImageResource(R.drawable.cosmetics);
        }else if (categoryModel.getName().equals("Detergent")) {
            holder.imageView.setImageResource(R.drawable.detergent);
        }else if (categoryModel.getName().equals("Kitchen")) {
            holder.imageView.setImageResource(R.drawable.grocerycart);
        }else if (categoryModel.getName().equals("Milk")) {
            holder.imageView.setImageResource(R.drawable.milk);
        }else if (categoryModel.getName().equals("Water")) {
            holder.imageView.setImageResource(R.drawable.waterbottle);
        }else if (categoryModel.getName().equals("Personal hygiene")) {
            holder.imageView.setImageResource(R.drawable.amenities);
        }else if (categoryModel.getName().equals("Cola")) {
            holder.imageView.setImageResource(R.drawable.softdrink);
        }else if (categoryModel.getName().equals("Tomato Paste")) {
            holder.imageView.setImageResource(R.drawable.ketchup);
        }else {
            holder.imageView.setImageResource(R.drawable.other);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsFragment productsFragment = new ProductsFragment();
                Bundle b = new Bundle();
                if (categoryModel.getName().equals("Chips") ){
                    b.putString("cate",categoryModel.getName()+" ");
                }else if (categoryModel.getName().equals("Personal hygiene")){
                    b.putString("cate","Personal hygiene (shampoo,deodorant..)");

                }

                else{
                    b.putString("cate",categoryModel.getName());
                }
                productsFragment.setArguments(b);
                AppCompatActivity appCompatActivity =(AppCompatActivity)v.getContext();
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, productsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return  categoryModelList == null ? 0 : categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameCategory);
            imageView = itemView.findViewById(R.id.imageCategory);

        }
    }
}
