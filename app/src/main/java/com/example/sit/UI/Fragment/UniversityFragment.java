package com.example.sit.UI.Fragment;

import static com.growingio.android.sdk.track.utils.ThreadUtils.runOnUiThread;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.sit.Adapter.HomeAdapter;
import com.example.sit.Const;
import com.example.sit.Entity.Goods;
import com.example.sit.Entity.GoodsPhoto;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.Utils.JsonUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @Author: Iced
 * @Date: 2022/3/22 11:08
 */
public class UniversityFragment extends Fragment {

    private int space = 5;
    static List<Goods> goodsList = new ArrayList<>();
    static List<List<GoodsPhoto>> photoList = new ArrayList<>();

    List<String> imgs = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    List<String> prices = new ArrayList<>();
    List<Integer> goods_id = new ArrayList<>();
    static HomeAdapter homeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_index_university, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRequest();

        homeAdapter = new HomeAdapter(imgs, titles, prices, goods_id, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.university_item);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new space_item(space));
        recyclerView.setAdapter(homeAdapter);
        RefreshLayout refreshLayout = view.findViewById(R.id.university_refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                imgs.clear();
                titles.clear();
                prices.clear();
                goods_id.clear();
                initRequest();
                homeAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
    }

    private void initRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Api.indexUniversity(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String res = response.body().string();
                        int code = JsonUtil.getStatusCode(res);
                        String msg = JsonUtil.getMessage(res);
                        if (code == Const.SuccessCode) {
                            Log.v(Const.RequestTag, msg);
                            JSONArray data = JsonUtil.getDataArray(res);
                            Gson gson = new Gson();
                            goodsList.clear();
                            goodsList = gson.fromJson(data.toString(), new TypeToken<List<Goods>>(){}.getType());
                        }
                        response.body().close();
                        photoList.clear();
                        for (Goods good : goodsList) {
                            Log.v("good --->  ", good.toString());
                            String good_id = String.valueOf(good.getId());
                            String resP = Api.getPhoto(good_id).body().string();
                            String msgP = JsonUtil.getMessage(res);
                            Log.v("GoodsPhoto ---> ", msgP);
                            JSONArray data = JsonUtil.getDataArray(resP);
                            Gson gson = new Gson();
                            photoList.add(gson.fromJson(data.toString(), new TypeToken<List<GoodsPhoto>>(){}.getType()));
                        }
                        initData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                homeAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        }).start();

    }

    private void initData() {

        int size = goodsList.size();
        for (int i = 0; i < size; i++) {
            String path = photoList.get(i).get(0).getPhoto_url();
            String uri = Const.goodsUrl + path;
            imgs.add(uri);
            String title = goodsList.get(i).getIntroduction();
            goods_id.add(goodsList.get(i).getId());
            titles.add(title.length() > 10 ? title.substring(0, 10) : title);
            prices.add(String.valueOf(goodsList.get(i).getPrice()));
        }

        Log.v("GoodsList ---> ", goodsList.toString());
        Log.v("PhotoList ---> ", photoList.toString());
        Log.v("imgs ---> ", imgs.toString());
        Log.v("titles ---> ", titles.toString());
        Log.v("prices ---> ", prices.toString());
    }

    private class space_item extends RecyclerView.ItemDecoration {
        private int space = 5;
        public space_item(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.bottom = space;
            outRect.top = space;
        }
    }
}
