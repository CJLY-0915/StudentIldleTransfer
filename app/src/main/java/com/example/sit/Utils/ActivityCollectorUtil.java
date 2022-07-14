package com.example.sit.Utils;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

// Activity的管理工具类
public class ActivityCollectorUtil extends Application {

    private static List<Activity> activityList = new ArrayList<Activity>();
    // 单例
    private static ActivityCollectorUtil instance;

    public ActivityCollectorUtil(){
        super();
    }

    public static ActivityCollectorUtil getInstance(){
        if (null == instance) {
            instance = new ActivityCollectorUtil();
        }
        return instance;
    }

    // 每个Activity在onCreate时都添加到这个List中
    public void addActivity(Activity av){
        activityList.add(av);
    }

    // 遍历,全局结束所有的Activity
    public void finishAll(){
        for (Activity activity:activityList){
            activity.finish();
        }
        System.exit(0);
    }
}
