package com.example.sit.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @Author: Iced
 * @Date: 2022/5/1 17:19
 */

public abstract class BaseFragment extends Fragment {
    private final String TAG = "lazy_fragment";
    private View mRootView;

    private boolean viewCreated = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayout(), container, false);
        }
        initView(mRootView);
        viewCreated = true;
        if (getUserVisibleHint()) {
            //由于onCreateView，在执行之前setUserVisibleHint已经执行，所以这里手动分发一次可见状态为true
            setUserVisibleHint(true);
        }
        return mRootView;
    }

    public abstract int getLayout();

    public abstract void initView(View view);
    /**
     *setUserVisibleHint会优先于所有生命周期的执行，
     *所以这里增加标志位viewCreated，视图创建了才执行函数
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (viewCreated) {
            if (isVisibleToUser) {
                //选中的时候可见，分发
                dispatchUserVisibleStatus(true);
            } else if (!isVisibleToUser) {
                //分发不可见
                dispatchUserVisibleStatus(false);
            }
        }
    }

    public void dispatchUserVisibleStatus(boolean isUserVisibleStatus) {

        if (isUserVisibleStatus) {
            onStartLoad();
        } else {
            onStopLoad();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 子类重写该方法来实现开始加载数据
     */
    public void onStartLoad() {
    }

    /**
     * 子类重写该方法来实现暂停数据加载
     */
    public void onStopLoad() {
    }
}
