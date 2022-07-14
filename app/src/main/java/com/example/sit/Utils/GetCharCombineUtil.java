package com.example.sit.Utils;

// 判断用户名和密码是否符合规则
public class GetCharCombineUtil {
    // 判断用户名的组成
    public Boolean getUserNameCharCombine(String s){
        char[] objStr = s.toCharArray();
        int intCount = 0,count = 0,otherCount = 0;
        Boolean res = false;
        for (int i = 0; i < s.length(); i++) {
            if (objStr[i] >= 'A' && objStr[i] <= 'Z'){
                count++;
            } else if (objStr[i] >= 'a' && objStr[i] <= 'z'){
                count++;
            } else if (objStr[i] >= '0' && objStr[i] <= '9') {
                intCount++;
            } else {
                otherCount++;
            }
        }
        if (count != 0 && intCount !=0){
            res = true;
        }
        return res;
    }
    // 判断密码的组成
    public Boolean getPwdCharCombine(String s){
        char[] objStr = s.toCharArray();
        int upperCount = 0,lowerCount = 0,intCount = 0,otherCount = 0;
        Boolean res = false;
        for (int i = 0; i < s.length(); i++) {
            if (objStr[i] >= 'A' && objStr[i] <= 'Z'){
                upperCount++;
            } else if (objStr[i] >= 'a' && objStr[i] <= 'z'){
                lowerCount++;
            } else if (objStr[i] >= '0' && objStr[i] <= '9') {
                intCount++;
            } else {
                otherCount++;
            }
        }
        if (upperCount != 0 && lowerCount != 0 && intCount !=0){
            if (otherCount == 0){
                res = true;
            }
        }
        return res;
    }
}
