package com.example.sit.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sit.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class BuyListBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater; // 根据Context生成View对象
    private List<HashMap<String,Object>> list; // 列表展示的数据源
    private Activity context; // 上下文对象
    public View view; // 布局

    public BuyListBaseAdapter(Activity av, List<HashMap<String,Object>> list){
        inflater = LayoutInflater.from(av);
        this.list = list;
        this.context = av;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HashMap<String,Object> getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View reUseView, ViewGroup viewGroup) {
        com.example.sit.Adapter.BuyListHolder byHolder = null;
        if (reUseView == null){
            byHolder = new com.example.sit.Adapter.BuyListHolder();
            reUseView = inflater.inflate(R.layout.my_buy_list_item,null);
            byHolder.buyProductImage = reUseView.findViewById(R.id.buy_productImage);
            byHolder.buyProductDes = reUseView.findViewById(R.id.buy_productDes);
            byHolder.buyProductPriceTips = reUseView.findViewById(R.id.buy_productPriceTip);
            byHolder.buyProductPrice = reUseView.findViewById(R.id.buy_productPrice);
            reUseView.setTag(byHolder);
        }else {
            byHolder =  (com.example.sit.Adapter.BuyListHolder) reUseView.getTag();
        }
        String photoUrl = (String)list.get(i).get("photo_url");
        String url = photoUrl.substring(0, 4);
        if (url.equals("http")){
            Picasso.with(context)
                    .load(photoUrl)
                    .placeholder(R.mipmap.xxz_logo)
                    .into(byHolder.buyProductImage);
        }else {
            File filePath = new File(url);
            Bitmap bitmap = BitmapFactory.decodeFile(photoUrl);
            byHolder.buyProductImage.setImageBitmap(bitmap);
        }
        byHolder.buyProductDes.setText((String)list.get(i).get("introduction"));
        byHolder.buyProductPrice.setText(""+(int)list.get(i).get("price"));

        return reUseView;
    }
}
