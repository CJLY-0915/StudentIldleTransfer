package com.example.sit.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sit.Const;
import com.example.sit.Dialogs.ImagePickDialog;
import com.example.sit.Entity.Address;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.Utils.JsonUtil;
import com.example.sit.Utils.NotifyUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BuyActivity extends AppCompatActivity {

    static String TAG = "BuyActivity ---> ";
    ImageView buyIv;
    ImageButton backBtn;
    Button editAddressBtn, buyBtn;
    TextView introTV, nameTV, numberTV, addressTV, priceTV;
    Map<String, Object> data = new HashMap<>();
    static List<Address> address;
    Address add;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        backBtn = findViewById(R.id.order_back);
        buyIv = findViewById(R.id.buy_img);
        editAddressBtn = findViewById(R.id.buy_edit_address);
        buyBtn = findViewById(R.id.buy_confirm);
        introTV = findViewById(R.id.buy_intro);
        nameTV = findViewById(R.id.buy_name);
        numberTV = findViewById(R.id.buy_number);
        addressTV = findViewById(R.id.buy_address);
        priceTV = findViewById(R.id.buy_price);

        Intent intent = getIntent();
        String price = intent.getStringExtra("price");
        String intro = intent.getStringExtra("intro");
        String img = intent.getStringExtra("image");
        int user_id = intent.getIntExtra("user_id", 0);
        int good_id = intent.getIntExtra("good_id", 0);

        Picasso.with(this)
               .load(img)
               .into(buyIv);
        priceTV.setText(price);
        introTV.setText(intro.length() > 15 ? intro.substring(0, 15) : intro);
        initRequest();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyActivity.this, AddressManageActivity.class);
                startActivity(intent);
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buy(good_id);
                finish();
            }
        });
    }

    private void initRequest() {
        Api.getAddress(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                int code = JsonUtil.getStatusCode(res);
                String msg = JsonUtil.getMessage(res);
                if (code == Const.SuccessCode) {
                    JSONArray d = JsonUtil.getDataArray(res);
                    Log.v(msg, d.toString());
                    Gson gson = new Gson();
                    address = gson.fromJson(d.toString(), new TypeToken<List<Address>>(){}.getType());
                    Log.v(TAG, address.toString());
                    initData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nameTV.setText((String)data.get("name"));
                            numberTV.setText((String)data.get("number"));
                            addressTV.setText((String)data.get("address"));
                        }
                    });
                }
            }
        });
    }

    private void initData() {
        add = address.get(0);
        data.put("name", add.getName());
        data.put("number", add.getNumber());
        data.put("address", add.getAddress());
    }

    private void buy(int good_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = null;
                try {
                    res = Api.buy(String.valueOf(good_id)).body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String msg = JsonUtil.getMessage(res);
                Log.v(TAG, msg);
                int code = JsonUtil.getStatusCode(res);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code == Const.SuccessCode) {
                            NotifyUtil.createNotify(BuyActivity.this, msg);
                        }
                    }
                });
            }
        }).start();
    }
}