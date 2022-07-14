package com.example.sit.UI.Fragment;

import static android.app.Activity.RESULT_OK;

import static com.growingio.android.sdk.track.utils.ThreadUtils.runOnUiThread;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sit.Const;
import com.example.sit.Dialogs.ImagePickDialog;
import com.example.sit.Entity.User;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.UI.Activity.MainActivity;
import com.example.sit.UI.Activity.MyReleaseActivity;
import com.example.sit.Utils.ActivityCollectorUtil;
import com.example.sit.Utils.BitmapUtil;
import com.example.sit.Utils.JsonUtil;
import com.example.sit.Utils.LoginUtil;
import com.example.sit.Utils.NotifyUtil;
import com.example.sit.Utils.SaveUrlToFileUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

// 发布闲置商品页面
public class PublishProductActivity extends Fragment {
    private static String TAG = "PublishProductActivity ---> ";
    public static final int FLAG_CODE = 10000;
//    public ImageView personalBtn; // 跳转到个人中心
    public Button publishSubmitBtn; // 发布
    public EditText publishProductDes; // 商品简介
    public ImageView publishImagePreview1; // 商品预览图1
    public ImageView publishImagePreview2; // 商品预览图2
    public ImageView publishImagePreview3; // 商品预览图3
    public TextView publishCurrentLocation; // 定位信息
    public TextView publishProductTextTips; // 提示信息
    public EditText publishProductPrice; // 商品价格
//    public LocationClient mLocationClient; // 定位客户端
//    public LocationUtil mListener =  new LocationUtil();
    public int productPrice = 0;
    public int maxSize = 3;
    public String[] imgPreviewUrl = new String[maxSize];
    public File[] images = new File[maxSize];
    public String productDes = "";
    HashMap<String, Object> publishProductMessage = new HashMap<>();
    Context context = getContext();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_release, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        personalBtn = view.findViewById(R.id.personal_page_image);
        publishSubmitBtn = view.findViewById(R.id.publish_product_submit);
        publishProductDes = view.findViewById(R.id.publish_product_text);
        publishProductTextTips = view.findViewById(R.id.publish_product_tips);
        publishImagePreview1 = view.findViewById(R.id.publish_product_img1);
        publishImagePreview2 = view.findViewById(R.id.publish_product_img2);
        publishImagePreview3 = view.findViewById(R.id.publish_product_img3);
        publishCurrentLocation = view.findViewById(R.id.publish_product_current_location);
        publishProductPrice = view.findViewById(R.id.publish_product_price);

        // TODO: 2022/4/3 进入发布商品页面,根据用户id查询出该用户的院校信息,作为发布地址
//        int accountId = LoginUtil.getAccountId(); // 当前用户的id
        User user = MainActivity.getUser();
        String testLocation = user.getUniversity();
        publishCurrentLocation.setText(testLocation);

        // 发布商品的监听
        publishSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 发布商品的描述信息
                productDes = publishProductDes.getText().toString();
                // 发布商品的价格
                String temps = publishProductPrice.getText().toString().trim().replaceAll(" ", "");
                if (!temps.equals("")){
                    productPrice = Integer.parseInt(temps);
                }
                if (productDes.equals("") || productPrice == 0 || imgPreviewUrl.equals("")){
                    NotifyUtil.createNotify(getActivity(),"请将信息填写完整");
                }else {
                    // TODO: 2022/4/3 根据用户id,将发布商品的信息存入数据库
                    /** 参数列表
                     imgUrl: 预览图地址(string数组)
                     introduction: 商品描述信息
                     price: 价格(int)
                     */
                    // 当前用户的id
//                    int currentUserId = LoginUtil.getAccountId();
                    publishProductMessage.put("price",String.valueOf(productPrice));
                    publishProductMessage.put("intro",productDes);
                    publishProductMessage.put("images",imgPreviewUrl);

                    initRequest();

                }
            }
        });

        // 上传预览图1的监听
        publishImagePreview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxSize = 3;
                ImagePickDialog.openAlbum(getActivity());
                maxSize--;
                publishProductTextTips.setVisibility(View.GONE);
                publishImagePreview2.setVisibility(View.VISIBLE);
            }
        });

        // 上传预览图2的监听
        publishImagePreview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxSize = 2;
                ImagePickDialog.openAlbum(getActivity());
                maxSize--;
                publishImagePreview3.setVisibility(View.VISIBLE);
            }
        });

        // 上传预览图3的监听
        publishImagePreview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxSize = 1;
                ImagePickDialog.openAlbum(getActivity());
                maxSize--;
            }
        });
    }

    private void initRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = null;
                try {
                    res = Api.releaseGoods(publishProductMessage).body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int code = JsonUtil.getStatusCode(res);
                Log.v(TAG, "" + code);
                String msg = JsonUtil.getMessage(res);
                Log.v(TAG,  msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code == Const.SuccessCode){
                            NotifyUtil.createNotify(getActivity(),msg);
                            RecommendFragment.homeAdapter.notifyDataSetChanged();
                            FollowFragment.homeAdapter.notifyDataSetChanged();
                            UniversityFragment.homeAdapter.notifyDataSetChanged();
                        }else{
                            NotifyUtil.createNotify(getActivity(),"发布失败,请刷新重试");
                        }
                    }
                });
            }
        }).start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK){
            // Api19以上,也就是安卓4.4版本以上处理返回的数据方式不同
            if (ImagePickDialog.getSDKVersionNumber() >= 19){
                if (maxSize == 2){
                    ImagePickDialog.handleSelectImage(data,getActivity() ,publishImagePreview1);
                    imgPreviewUrl[0] = ImagePickDialog.getGoalImageUrl();
                    images[0] = ImagePickDialog.getGoodFile();
                }
                if (maxSize == 1){
                    ImagePickDialog.handleSelectImage(data,getActivity(),publishImagePreview2);
                    imgPreviewUrl[1] = ImagePickDialog.getGoalImageUrl();
                    images[1] = ImagePickDialog.getGoodFile();
                }
                if (maxSize == 0){
                    ImagePickDialog.handleSelectImage(data,getActivity(),publishImagePreview3);
                    imgPreviewUrl[2] = ImagePickDialog.getGoalImageUrl();
                    images[2] = ImagePickDialog.getGoodFile();
                }
            }
        }
    }

    private File urlToFile(String imgUrl) {
        Bitmap bitmap = BitmapFactory.decodeFile(imgUrl);
        // 头像文件
        return SaveUrlToFileUtil.getFile(bitmap);
    }

}