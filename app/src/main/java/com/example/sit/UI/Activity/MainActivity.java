package com.example.sit.UI.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.example.sit.Adapter.MainAdapter;
import com.example.sit.Const;
import com.example.sit.Entity.Goods;
import com.example.sit.Entity.GoodsPhoto;
import com.example.sit.Entity.User;
import com.example.sit.Service.Api;
import com.example.sit.UI.Fragment.IndexFragment;
import com.example.sit.UI.Fragment.MessageFragment;
import com.example.sit.R;
import com.example.sit.UI.Fragment.PersonalCenterActivity;
import com.example.sit.UI.Fragment.PublishProductActivity;
import com.example.sit.Utils.BarStatusUtil;
import com.example.sit.Utils.JsonUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.hyphenate.easeui.EaseIM;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment;
import com.hyphenate.easeui.provider.EaseUserProfileProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends FragmentActivity {
    static User user = null;

    TabLayout mTabLayout;
    ViewPager2 mViewPager;

    List<Fragment> fragments = new ArrayList<>();
    String[] title = {"首页", "发闲置", "消息", "我的"};
    int[] icon = {R.drawable.index, R.drawable.release, R.drawable.message, R.drawable.personal};

    PublishProductActivity ppa = new PublishProductActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        initData();

        setContentView(R.layout.activity_main);

        BarStatusUtil.setBarBackgroundColor(MainActivity.this, R.color.light_green);

        user = (User) getIntent().getSerializableExtra("user");
        mTabLayout = findViewById(R.id.main_tab);
        mViewPager = findViewById(R.id.main_view_pager);

        fragments.add(new IndexFragment());
        fragments.add(ppa);
        fragments.add(new MessageFragment());
//        fragments.add(message);
        fragments.add(new PersonalCenterActivity());
        mViewPager.setUserInputEnabled(false);
        mViewPager.setOffscreenPageLimit(4);

        Intent intent = getIntent();
        int id = intent.getIntExtra("page", 3);
        mViewPager.setCurrentItem(id);

        mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), getLifecycle(), fragments));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("TAG", "tab position: " + tab.getPosition());
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.light_green);
                Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.tab_unselected);
                Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        new TabLayoutMediator(mTabLayout, mViewPager,true, false, new TabLayoutMediator.TabConfigurationStrategy() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setIcon(icon[position]).setText(title[position]);
            }
        }).attach();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ppa.onActivityResult(requestCode, resultCode, data);
    }

    public static User getUser() {
        return user;
    }

}