package com.elm.boycothelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elm.boycothelper.activites.MainActivity;
import com.elm.boycothelper.activites.ProducrsAllowedActivity;
import com.elm.boycothelper.R;
import com.elm.boycothelper.model.HomeModel;

import java.util.List;

public class HomeRvAdapter extends RecyclerView.Adapter<HomeRvAdapter.ViewHolder> {
    Context context;
    private List<HomeModel> homeModelList;

    public HomeRvAdapter(Context context, List<HomeModel> homeModelList) {
        this.context = context;
        this.homeModelList = homeModelList;
    }

    public void setItems(List<HomeModel>homeModelList, Context context){
        this.homeModelList= homeModelList;
        this.context=context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homerv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeModel homeModel = homeModelList.get(position);
        holder.name.setText(homeModel.getNameCate());
        holder.imageView.setImageResource(homeModel.getPhotoCate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeModel.getNameCate().equals(context.getString(R.string.product_checker))){
                    Intent go = new Intent(context, MainActivity.class);
                    context.startActivity(go);
                }else if (homeModel.getNameCate().equals(context.getString(R.string.Products_Can_Buy))){
                    Intent go = new Intent(context, ProducrsAllowedActivity.class);
                    context.startActivity(go);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeModelList == null ? 0 : homeModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.catName);
            imageView = itemView.findViewById(R.id.catPhoto);
        }
    }
}
