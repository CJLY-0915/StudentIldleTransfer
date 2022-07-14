package com.example.sit.Dialogs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.sit.R;
import com.example.sit.Utils.AddressManageDataUtil;

import java.util.HashMap;
import java.util.Map;

// 添加地址信息弹框
public class AddAddressPopDialog extends PopupWindow {
    public LayoutInflater inflater;
    public View view;
    public EditText userNameText;
    public EditText phoneText;
    public EditText addressText;
    public Button saveBtn;
    public Button cancelBtn;

    public AddAddressPopDialog(Activity av){
        super(av);
        inflater = LayoutInflater.from(av);
        if (inflater != null){
            view = inflater.inflate(R.layout.address_manage_add, null);
            userNameText = view.findViewById(R.id.address_manage_add_userName);
            phoneText = view.findViewById(R.id.address_manage_add_Phone);
            addressText = view.findViewById(R.id.address_manage_add_Address);
            saveBtn = view.findViewById(R.id.address_manage_add_save);
            cancelBtn = view.findViewById(R.id.address_manage_add_cancel);
            this.setContentView(view);
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            // 设置透明度,让背景变暗
            WindowManager.LayoutParams params = av.getWindow().getAttributes();
            params.alpha = 0.5f;
            av.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            av.getWindow().setAttributes(params);
            // 取消按钮的监听
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            // 销毁弹窗后将透明度恢复
            this.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    params.alpha = 1.0f;
                    av.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    av.getWindow().setAttributes(params);
                }
            });
            // 添加按钮的监听
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ut = userNameText.getText().toString();
                    String pt = phoneText.getText().toString();
                    String at = addressText.getText().toString();
                    HashMap<String, String> resMap = AddressManageDataUtil.handleAddMessageData(av, ut, pt, at);
                    mItemClickListener.itemOnClickListener(view,resMap);
                }
            });
        }
    }

    private ItemOnClickListener mItemClickListener;
    public void setItemOnEditClickListener(ItemOnClickListener listener){
        this.mItemClickListener = listener;
    }
    public interface ItemOnClickListener{
        public void itemOnClickListener(View view, Map<String,String> addMessageMap);
    }
}
