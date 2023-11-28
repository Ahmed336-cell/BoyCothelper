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
import com.elm.boycothelper.pojo.Constants;

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
        if (categoryModel.getName().equals("جبنة")){
            holder.imageView.setImageResource(R.drawable.cheese);
        } else if (categoryModel.getName().equals("شيبس")) {
            holder.imageView.setImageResource(R.drawable.chips);
        }else if (categoryModel.getName().equals("شوكولاتة")) {
            holder.imageView.setImageResource(R.drawable.chocolate);
        }else if (categoryModel.getName().equals("قهوة")) {
            holder.imageView.setImageResource(R.drawable.coffee);
        }else if (categoryModel.getName().equals("مستحضرات تجميل")) {
            holder.imageView.setImageResource(R.drawable.cosmetics);
        }else if (categoryModel.getName().equals("منظفات")) {
            holder.imageView.setImageResource(R.drawable.detergent);
        }else if (categoryModel.getName().equals("مستلزمات مطبخ")) {
            holder.imageView.setImageResource(R.drawable.grocerycart);
        }else if (categoryModel.getName().equals("لبن")) {
            holder.imageView.setImageResource(R.drawable.milk);
        }else if (categoryModel.getName().equals("مياة")) {
            holder.imageView.setImageResource(R.drawable.waterbottle);
        }else if (categoryModel.getName().equals("النظافة الشخصية")) {
            holder.imageView.setImageResource(R.drawable.amenities);
        }else if (categoryModel.getName().equals("كولا")) {
            holder.imageView.setImageResource(R.drawable.softdrink);
        }else if (categoryModel.getName().equals("صلصة طماطم")) {
            holder.imageView.setImageResource(R.drawable.ketchup);
        }else {
            holder.imageView.setImageResource(R.drawable.other);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsFragment productsFragment = new ProductsFragment();
                Bundle b = new Bundle();
                if (categoryModel.getName().equals("شيبس") ){
                    b.putString(Constants.CATEGORY_KEY,categoryModel.getDescription()+" ");
                }else if (categoryModel.getName().equals("Personal hygiene")){
                    b.putString(Constants.CATEGORY_KEY,"Personal hygiene (shampoo,deodorant..)");

                }

                else{
                    b.putString(Constants.CATEGORY_KEY,categoryModel.getDescription());
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
