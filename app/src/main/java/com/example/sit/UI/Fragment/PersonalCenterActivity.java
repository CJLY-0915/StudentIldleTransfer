package com.example.sit.UI.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sit.Const;
import com.example.sit.Entity.User;
import com.example.sit.Service.Api;
import com.example.sit.UI.Activity.AddressManageActivity;
import com.example.sit.UI.Activity.EditPersonalMessageActivity;
import com.example.sit.UI.Activity.FollowActivity;
import com.example.sit.UI.Activity.MainActivity;
import com.example.sit.UI.Activity.MyBuyActivity;
import com.example.sit.UI.Activity.MyReleaseActivity;
import com.example.sit.UI.Activity.MySellActivity;
import com.example.sit.Utils.JsonUtil;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.example.sit.R;
import com.example.sit.Utils.ActivityCollectorUtil;
import com.example.sit.Utils.BarStatusUtil;
import com.example.sit.Utils.LoginUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// App个人中心界面
public class PersonalCenterActivity extends Fragment {
    public Button settingBtn;  // 右上角设置按钮
    public ImageView headerImage; // 头像
    public TextView nicknameText; // 昵称
    public Button addressManageBtn; // 地址管理按钮
    public Button myFollowBtn; // 我的关注按钮
    public ImageView releaseBtn; // 我的发布
    public ImageView sellBtn; // 我卖出的
    public ImageView buyBtn; // 我买到的
    public User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.personal_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingBtn = view.findViewById(R.id.setting_btn);
        headerImage = view.findViewById(R.id.header_image);
        nicknameText = view.findViewById(R.id.nickName);
        addressManageBtn = view.findViewById(R.id.myTransaction_btn);
        myFollowBtn = view.findViewById(R.id.myFollow_btn);
        releaseBtn = view.findViewById(R.id.img_myRelease);
        sellBtn = view.findViewById(R.id.img_mySale);
        buyBtn = view.findViewById(R.id.img_myBuy);
//        publishBtn = view.findViewById(R.id.sale_page_image);

        /** 返回参数列表
         headerImg: 用户的头像地址
         nickName: 用户昵称
         */
        user = MainActivity.getUser();
        String headerImg = Const.avatarUrl + user.getAvatar_url();
        String nickName = user.getNickname();
        // 根据url设置头像
        Picasso.with(getContext())
                .load(headerImg)
                .into(headerImage);

        // 设置登录用户的昵称
        nicknameText.setText(nickName);

        // 点击右上角的设置,跳转到编辑详细信息界面
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(getActivity(),
                        EditPersonalMessageActivity.class);
                editIntent.putExtra("Info", user);
                startActivity(editIntent);
            }
        });

        // 点击地址管理,跳转到地址管理界面
        addressManageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addressIntent = new Intent(getActivity(),
                        AddressManageActivity.class);
                startActivity(addressIntent);
            }
        });

        // 点击我的关注,跳转到关注列表界面
        myFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followIntent = new Intent(getActivity(),
                        FollowActivity.class);
                startActivity(followIntent);
            }
        });

        // 点击我的发布,跳转到发布的商品页面
        releaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent releaseIntent = new Intent(getActivity(),
                        MyReleaseActivity.class);
                startActivity(releaseIntent);
            }
        });

        // 点击我卖出的,跳转到我卖出的商品页面
        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MySellActivity.class);
                startActivity(intent);
            }
        });

        // 点击我买到的,跳转到我买到的商品页面
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyBuyActivity.class);
                startActivity(intent);
            }
        });

        RefreshLayout refreshLayout = view.findViewById(R.id.personal_refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                String headerImg = Const.avatarUrl + user.getAvatar_url();
                Picasso.with(getContext())
                        .load(headerImg)
                        .into(headerImage);
                nicknameText.setText(user.getNickname());
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
    }
}