package com.example.sit.Utils;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sit.Entity.Address;
import com.example.sit.R;

import java.util.HashMap;
import java.util.Map;

// 地址管理数据处理工具类
public class AddressManageDataUtil {
    public static EditText addressUserNameText; // 姓名
    public static EditText addressPhoneText; // 联系方式
    public static EditText addressFullAdsText; // 详细地址
    public static TextView addressName;
    public static TextView addressPhone;
    public static TextView addressFullAds;

    // 封装修改的地址信息,传递给后端
    public static HashMap<String,String> getEditAddressMessage(String name,String phone,String address){
        HashMap<String, String> editAddressMessageMap = new HashMap<>();
        editAddressMessageMap.put("name",name);
        editAddressMessageMap.put("number",phone);
        editAddressMessageMap.put("address",address);
        return editAddressMessageMap;
    }

    // 设置编辑界面回显的数据
    public static void setFirstShowMessage(Map<String, Object> map, View view){
        addressUserNameText = view.findViewById(R.id.address_manage_edit_userName);
        addressPhoneText = view.findViewById(R.id.address_manage_edit_userPhone);
        addressFullAdsText = view.findViewById(R.id.address_manage_edit_userAddress);
        addressUserNameText.setText((String)map.get("address_userName"));
        addressPhoneText.setText((String)map.get("address_phone"));
        addressFullAdsText.setText((String)map.get("address_fullAddress"));
    }

    // 设置更新后刷新的数据
    public static void setUploadMessage(Address map,Activity av){
        addressName = av.findViewById(R.id.address_userName);
        addressPhone = av.findViewById(R.id.address_phone);
        addressFullAds = av.findViewById(R.id.address_fullAddress);
        addressName.setText(map.getName());
        addressPhone.setText(map.getNumber());
        addressFullAds.setText(map.getAddress());
    }

    // 添加新地址的数据处理
    public static HashMap<String,String> handleAddMessageData(Activity av,String nameText,
                                                              String phoneText,String adsText){
        HashMap<String, String> resMap = new HashMap<>();
        String name = nameText.trim().replaceAll(" ", "");
        String number = phoneText.trim().replaceAll(" ", "");
        String address = adsText.trim().replaceAll(" ", "");
        if (name.equals("") || number.equals("") || address.equals("")){
            if (name.equals("")){
                NotifyUtil.createNotify(av,"联系人姓名不能为空");
            }else if (number.equals("")){
                NotifyUtil.createNotify(av,"联系电话不能为空");
            }else{
                NotifyUtil.createNotify(av,"详细地址不能为空");
            }
        }else {
            resMap.put("name",name);
            resMap.put("number",number);
            resMap.put("address",address);
        }
        return resMap;
    }
}
