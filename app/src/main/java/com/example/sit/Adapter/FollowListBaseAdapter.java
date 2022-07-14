package com.example.sit.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sit.Const;
import com.example.sit.Entity.User;
import com.example.sit.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowListBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Map<String, Object>> list;
    private Activity context;
    public View view; // 布局

    public FollowListBaseAdapter(Activity av, List<Map<String, Object>> list){
        this.inflater = LayoutInflater.from(av);
        this.list = list;
        this.context = av;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
         return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View reUseView, ViewGroup viewGroup) {
        com.example.sit.Adapter.FollowListHolder holder = null;
        if (reUseView == null){
            holder = new com.example.sit.Adapter.FollowListHolder();
            reUseView = inflater.inflate(R.layout.follow_list_item,null);
            holder.headerImage = reUseView.findViewById(R.id.follow_user_header_image);
            holder.nicknameText = reUseView.findViewById(R.id.follow_user_nickname);
            holder.describeText = reUseView.findViewById(R.id.follow_user_des);
            reUseView.setTag(holder);
        }else {
            holder = (com.example.sit.Adapter.FollowListHolder) reUseView.getTag();
        }
        String avatar_url = Const.avatarUrl + list.get(i).get("avatar_url");
        String url = avatar_url.substring(0, 4);
        if (url.equals("http")){
            Picasso.with(context)
                    .load(avatar_url)
                    .placeholder(R.mipmap.xxz_logo)
                    .into(holder.headerImage);
        }else {
//            File filePath = new File(url);
            Bitmap bitmap = BitmapFactory.decodeFile(avatar_url);
            holder.headerImage.setImageBitmap(bitmap);
        }
        holder.nicknameText.setText((String)list.get(i).get("nickname"));
        String introduction = (String)list.get(i).get("introduction");
        if (introduction == null || introduction.equals("")){
            holder.describeText.setText("该用户未填写简介");
        }else {
            holder.describeText.setText(introduction);
        }
        return reUseView;
    }
}
