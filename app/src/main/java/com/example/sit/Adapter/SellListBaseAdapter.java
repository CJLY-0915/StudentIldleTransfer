package com.example.sit.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;
import com.example.sit.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SellListBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater; // 根据Context生成View对象
    private List<HashMap<String,Object>> list; // 列表展示的数据源
    private Activity context; // 上下文对象
    public View view; // 布局

    public SellListBaseAdapter(Activity av,List<HashMap<String,Object>> list){
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
        com.example.sit.Adapter.SellListHolder slHolder = null;
        if (reUseView == null){
            slHolder = new com.example.sit.Adapter.SellListHolder();
            reUseView = inflater.inflate(R.layout.my_sell_list_item,null);
            slHolder.sellProductImage = reUseView.findViewById(R.id.sell_productImage);
            slHolder.sellProductDes = reUseView.findViewById(R.id.sell_productDes);
            slHolder.sellProductPriceTips = reUseView.findViewById(R.id.sell_productPriceTip);
            slHolder.sellProductPrice = reUseView.findViewById(R.id.sell_productPrice);
            reUseView.setTag(slHolder);
        }else {
            slHolder =  (com.example.sit.Adapter.SellListHolder) reUseView.getTag();
        }
        String photoUrl = (String)list.get(i).get("photo_url");
        String url = photoUrl.substring(0, 4);
        if (url.equals("http")){
            Picasso.with(context)
                    .load(photoUrl)
                    .placeholder(R.mipmap.xxz_logo)
                    .into(slHolder.sellProductImage);
        }else {
            File filePath = new File(url);
            Bitmap bitmap = BitmapFactory.decodeFile(photoUrl);
            slHolder.sellProductImage.setImageBitmap(bitmap);
        }
        slHolder.sellProductDes.setText((String)(list.get(i).get("introduction")));
        slHolder.sellProductPrice.setText(""+(int)list.get(i).get("price"));

        return reUseView;
    }


    public void updata(List<HashMap<String,Object>> newlist){
        if(null==newlist){
            return;
        }else {

            list.clear();
            list.addAll(newlist);
            notifyDataSetChanged();
        }

    }
}
