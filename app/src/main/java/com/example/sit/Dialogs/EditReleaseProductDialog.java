package com.example.sit.Dialogs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.squareup.picasso.Picasso;
import com.example.sit.R;

import java.io.File;
import java.util.HashMap;

public class EditReleaseProductDialog extends PopupWindow {
    public LayoutInflater inflater;
    public View view;
    public ImageView productImageView; // 商品预览图
    public EditText productDesText; // 商品描述信息
    public EditText productPriceText; // 商品价格
    public Button saveBtn; // 保存
    public Button cancelBtn; // 取消
    public String introduction = "";
    public int price = 0;

    public EditReleaseProductDialog(Activity av, HashMap<String,Object> currentMessage) {
        super(av);
        inflater = LayoutInflater.from(av);
        if (inflater != null) {
            view = inflater.inflate(R.layout.my_release_edit, null);
            productImageView = view.findViewById(R.id.release_edit_img);
            productDesText = view.findViewById(R.id.release_edit_proDes);
            productPriceText = view.findViewById(R.id.release_edit_proPrice);
            saveBtn = view.findViewById(R.id.release_edit_save);
            cancelBtn = view.findViewById(R.id.release_edit_cancel);
            // 初始化时,回显信息
            if (currentMessage != null){
                String photoUrl = (String)currentMessage.get("photo_url");
                String url = photoUrl.substring(0, 4);
                if (url.equals("http")){
                    Picasso.with(av)
                            .load(photoUrl)
                            .placeholder(R.mipmap.xxz_logo)
                            .into(productImageView);
                }else {
                    File filePath = new File(url);
                    Bitmap bitmap = BitmapFactory.decodeFile(photoUrl);
                    productImageView.setImageBitmap(bitmap);
                }
                introduction = (String)currentMessage.get("introduction");
                productDesText.setText(introduction);
                price = (int)currentMessage.get("price");
                productPriceText.setText(""+price);
            }
            this.setContentView(view);
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            ColorDrawable cd = new ColorDrawable(av.getResources().getColor(R.color.white));
            this.setBackgroundDrawable(cd);

            // 更换预览图片的监听
            productImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mImageClickListener.imageOnClickListener(view,productImageView);
                }
            });

            // 取消按钮的监听
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            // 保存按钮的监听
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pd = productDesText.getText().toString();
                    String pp = productPriceText.getText().toString();
                    HashMap<String, Object> resMap = new HashMap<>();
                    resMap.put("introduction",pd);
                    resMap.put("price",Integer.parseInt(pp));
                    mSaveClickListener.saveClickListener(view,resMap);
                }
            });
        }
    }

    private ImageClickListener mImageClickListener;
    private saveEditClickListener mSaveClickListener;

    public void setImageClickListener(ImageClickListener listener){
        this.mImageClickListener = listener;
    }

    public void saveClickListener(saveEditClickListener listener){
        this.mSaveClickListener = listener;
    }

    public interface ImageClickListener{
        public void imageOnClickListener(View view,ImageView productImageView);
    }

    public interface saveEditClickListener{
        public void saveClickListener(View view,HashMap<String, Object> resMap);
    }
}
