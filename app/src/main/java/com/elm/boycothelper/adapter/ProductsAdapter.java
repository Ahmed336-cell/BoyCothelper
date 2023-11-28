package com.elm.boycothelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elm.boycothelper.R;
import com.elm.boycothelper.ROOM.ProductEntity;
import com.elm.boycothelper.model.ProductModel;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    Context context;
    private List<ProductModel> productModelsList;

    public ProductsAdapter(Context context, List<ProductModel> productModelsList) {
        this.context = context;
        this.productModelsList = productModelsList;
    }
    public void setFilterd(List<ProductModel>filterd){
        this.productModelsList=filterd;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_display_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel productModel = productModelsList.get(position);
        holder.name.setText(productModel.getName());
        holder.description.setText(productModel.getDescription());
        Picasso.get().load(productModel.getImageUrl()).placeholder(R.drawable.loading100).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return  productModelsList == null ? 0 : productModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,description;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameProduct);
            description = itemView.findViewById(R.id.descriptionText);
            imageView = itemView.findViewById(R.id.imageproduct);
        }
    }
}