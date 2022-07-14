package com.example.sit.Adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.sit.Entity.Address;
import com.example.sit.R;
import com.example.sit.Utils.AddressManageDataUtil;
import com.example.sit.Utils.NotifyUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 展示收货地址的列表适配器
public class ListViewBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater; // 根据Context生成View对象
    private List<Map<String, Object>> list; // 列表展示的数据源
    private Activity context; // 上下文对象
    public View view; // 布局
    public Button editSaveBtn; // 保存
    public EditText addressUserNameText; // 姓名
    public EditText addressPhoneText; // 联系方式
    public EditText addressFullAdsText; // 详细地址

    public ListViewBaseAdapter(Activity av,List<Map<String, Object>> list) {
        this.inflater = LayoutInflater.from(av);
        this.list = list;
        this.context = av;
    }

    /**
        4个重写方法,分别是:
            (1)获得当前列表的总数
            (2)获得当前点击项的id
            (3)获得当前点击项的所有内容
            (4)设置每个item中每个控件的数据,点击事件,回调等;每个item被划出屏幕外时也会调用
     */

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
    // reUseView:将之前加载好的布局进行缓存,如果不为null,则说明有之前缓存的布局,在屏幕滑动时就不会重新加载
    public View getView(int i, View reUseView, ViewGroup viewGroup) {
        // 自定义的文本控件容器
         com.example.sit.Adapter.TextViewHolder tvHolder = null;
        if (reUseView == null){
            tvHolder = new com.example.sit.Adapter.TextViewHolder();
            // 加载布局并给每个控件赋值
            reUseView = inflater.inflate(R.layout.address_list_item, null);
            tvHolder.addressUserName = reUseView.findViewById(R.id.address_userName);
            tvHolder.addressPhone = reUseView.findViewById(R.id.address_phone);
            tvHolder.addressFullAddress = reUseView.findViewById(R.id.address_fullAddress);
            tvHolder.editAddressMessageBtn = reUseView.findViewById(R.id.address_edit_message);
            tvHolder.deleteAddressMessageBtn = reUseView.findViewById(R.id.address_delete);
            // 为holder添加标记,如果tag存在,就将view重新赋值给holder
            reUseView.setTag(tvHolder);
        }else {
            tvHolder = (com.example.sit.Adapter.TextViewHolder) reUseView.getTag();
        }
        // 取出数据源中的数据,赋值后回显到页面上
        tvHolder.addressUserName.setText((String)list.get(i).get("address_userName"));
        tvHolder.addressPhone.setText((String)list.get(i).get("address_phone"));
        tvHolder.addressFullAddress.setText((String)list.get(i).get("address_fullAddress"));
        // 地址的id,不显示到页面,用于操作数据
        tvHolder.addressId = Integer.parseInt((list.get(i).get("address_id")).toString());

        // 编辑按钮的监听
        tvHolder.editAddressMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                System.out.println(i);
                // 当前点击项所有需要的数据
//                Address currentAddressMap = (Address) getItem(i);
                HashMap<String,Object> currentAddressMap = (HashMap<String,Object>)getItem(i);
                // 实例化弹窗,并且将当前地址的id传递过去,用于修改后保存到数据库
                int id = Integer.parseInt(currentAddressMap.get("address_id").toString());
                PopupWindow popupWindow = new PopupWindow();
                // 这个视图布局绑定了弹窗
                view = LayoutInflater.from(context).inflate(R.layout.address_manage_edit, null);
                // 第一次点击进入编辑时回显数据
                AddressManageDataUtil.setFirstShowMessage(currentAddressMap,view);
                editSaveBtn = view.findViewById(R.id.edit_address_save);
                addressUserNameText = view.findViewById(R.id.address_manage_edit_userName);
                addressPhoneText = view.findViewById(R.id.address_manage_edit_userPhone);
                addressFullAdsText = view.findViewById(R.id.address_manage_edit_userAddress);
                // 点击保存按钮的监听
                editSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v2) {
                        // 修改的3项属性
                        String userNameText = addressUserNameText.getText().toString();
                        String phoneText = addressPhoneText.getText().toString();
                        String fullAdsText = addressFullAdsText.getText().toString();
                        String name = userNameText.trim().replaceAll(" ", "");
                        String phone = phoneText.trim().replaceAll(" ", "");
                        String address = fullAdsText.trim().replaceAll(" ", "");
                        if (name.equals("") || phone.equals("") || address.equals("")){
                            if (name.equals("")){
                                NotifyUtil.createNotify(context,"姓名不能为空");
                            }else if (phone.equals("")){
                                NotifyUtil.createNotify(context,"联系方式不能为空");
                            }else if (address.equals("")){
                                NotifyUtil.createNotify(context,"详细地址不能为空");
                            }
                        }else{
                            // 封装好的修改后的信息
                            HashMap<String, String> map = AddressManageDataUtil.getEditAddressMessage(name, phone, address);
                            // 销毁弹窗
                            popupWindow.dismiss();
                            // 回调监听,在AddressManageActivity中设置新数据
                            mItemClickListener.itemOnClickListener(v1,i,map);
                        }
                    }
                });
                ColorDrawable backColor = new ColorDrawable(context.getResources().getColor(R.color.white));
                popupWindow.setBackgroundDrawable(backColor);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setContentView(view);
                popupWindow.setFocusable(true);
                popupWindow.setWidth(600);
                popupWindow.setHeight(450);
                popupWindow.showAsDropDown(v1);
            }
        });

        // 删除按钮的监听
        tvHolder.deleteAddressMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Address currentAddressMap = (Address)getItem(i);
                Map<String,Object> currentAddressMap = (HashMap<String,Object>)getItem(i);
                int addressId = Integer.parseInt(currentAddressMap.get("address_id").toString());
                mItemDeleteClickListener.itemOnDeleteClickListener(view,addressId,i);
            }
        });
        // 将监听,数据等全部设置好后再返回view
        return reUseView;
    }

    private ItemOnClickListener mItemClickListener;
    private ItemOnDeleteClickListener mItemDeleteClickListener;
    // 将Activity中的按钮监听和适配器绑定
    public void setItemOnEditClickListener(ItemOnClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setItemOnDeleteClickListener(ItemOnDeleteClickListener listener){
        this.mItemDeleteClickListener = listener;
    }

    // 编辑按钮触发监听后的回调
    public interface ItemOnClickListener{
        /**
         view: 当前是哪个按钮触发的监听
         i: 自定义参数,这里是需要修改的地址信息的id
         */
        public void itemOnClickListener(View view, int i, Map<String,String> newMessageMap);
    }

    // 删除按钮触发监听后的回调
    public interface ItemOnDeleteClickListener{
        public void itemOnDeleteClickListener(View view,int i1,int i2);
    }
}
