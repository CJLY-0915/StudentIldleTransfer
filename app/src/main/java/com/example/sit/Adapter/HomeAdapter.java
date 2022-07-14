package com.example.sit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sit.R;
import com.example.sit.UI.Activity.DetailActivity;
import com.example.sit.UI.Activity.MainActivity;
import com.example.sit.UI.Activity.SearchActivity;
import com.example.sit.UI.Fragment.RecommendFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @Author: Iced
 * @Date: 2022/4/26 16:25
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHodler> {

    private List<String> imgs;
    private List<String> titles;
    private List<String> prices;
    private List<Integer> goods_id;
    Context context;

    public HomeAdapter(List<String> imgs, List<String> titles, List<String> prices, List<Integer> goods_id, Context context) {
        this.imgs = imgs;
        this.titles = titles;
        this.prices = prices;
        this.goods_id = goods_id;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.goods_item, null);
        HomeAdapter.MyViewHodler myViewHodler = new HomeAdapter.MyViewHodler(view);
        return myViewHodler;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHodler holder, int position) {
        Picasso.with(context)
                .load(imgs.get(position))
                .into(holder.img);
        holder.title.setText(titles.get(position));
        holder.price.setText(prices.get(position));
        int id = goods_id.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("goods_id", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class MyViewHodler extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title, price;
        public MyViewHodler(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.home_item_img);
            title = itemView.findViewById(R.id.home_item_title);
            price = itemView.findViewById(R.id.home_item_price);
        }
    }
}
