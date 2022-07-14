package com.example.sit.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.example.sit.Dialogs.EditReleaseProductDialog;
import com.example.sit.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class ReleaseListBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater; // 根据Context生成View对象
    private List<HashMap<String,Object>> list; // 列表展示的数据源
    private Activity context; // 上下文对象
    public View view; // 布局

    public ReleaseListBaseAdapter(Activity av,List<HashMap<String,Object>> list) {
        this.inflater = LayoutInflater.from(av);
        this.list = list;
        this.context = av;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HashMap<String, Object> getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View reUseView, ViewGroup viewGroup) {
        ReleaseListHolder rlHolder = null;
        if (reUseView == null){
            rlHolder = new ReleaseListHolder();
            reUseView = inflater.inflate(R.layout.my_release_list_item,null);
            rlHolder.productImg = reUseView.findViewById(R.id.release_productImage);
            rlHolder.productDesText = reUseView.findViewById(R.id.release_productDes);
            rlHolder.productPriceText = reUseView.findViewById(R.id.release_productPrice);
            reUseView.setTag(rlHolder);
        }else {
            rlHolder = (ReleaseListHolder) reUseView.getTag();
        }
        // 商品的id
        rlHolder.productId = (int)list.get(i).get("goods_id");
        String photoUrl = (String)list.get(i).get("photo_url");
        String url = photoUrl.substring(0, 4);
        if (url.equals("http")){
            Picasso.with(context)
                    .load(photoUrl)
                    .placeholder(R.mipmap.xxz_logo)
                    .into(rlHolder.productImg);
        }else {
            File filePath = new File(url);
            Bitmap bitmap = BitmapFactory.decodeFile(photoUrl);
            rlHolder.productImg.setImageBitmap(bitmap);
        }
        rlHolder.productDesText.setText((String)list.get(i).get("introduction"));
        rlHolder.productPriceText.setText(""+(int)list.get(i).get("price"));

        return reUseView;
    }
}
