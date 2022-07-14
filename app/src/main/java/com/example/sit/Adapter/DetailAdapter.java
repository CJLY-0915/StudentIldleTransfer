package com.example.sit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sit.R;
import com.example.sit.UI.Activity.DetailActivity;
import com.example.sit.views.RoundRectImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Iced
 * @Date: 2022/5/2 17:10
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ImageHolder> {

    Context context;
    List<String> images;

    public DetailAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_img_item, null);
        DetailAdapter.ImageHolder myViewHodler = new ImageHolder(view);
        return myViewHodler;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Picasso.with(context)
                .load(images.get(position))
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ImageHolder extends RecyclerView.ViewHolder{

        ImageView img;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.detail_img);
        }
    }
}
