//package com.example.sit.Adapter;
//
//import com.baidu.location.BDAbstractLocationListener;
//import com.baidu.location.BDLocation;
//
//// 定位服务的工具类
//public class LocationUtil extends BDAbstractLocationListener {
//    public double latitude; // 经度
//    public double longitude; // 纬度
//
//    // 获取位置数据的监听
//    @Override
//    public void onReceiveLocation(BDLocation location) {
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//        if (latitude != 0 && longitude != 0){
//            System.out.println("经度--->"+latitude);
//            System.out.println("纬度--->"+longitude);
//            System.out.println(location.getAddrStr());
//        }
//    }
//
////    private HashMap<String,String> getCurrentLocation() throws IOException {
////        URL url = new URL(
////        "https://api.map.baidu.com/reverse_geocoding/v3/" +
////            "?ak=qRfK2VoHWGVjfpX55F4R0OwGVr0LCdWx&output=json&coordtype=wgs84ll&" +
////            "location=30.65613,104.054018&" +
////            "mcode=5C:88:E2:25:72:93:04:DB:66:5B:D9:62:D9:79:0B:17:AB:B1:54:88;com.xxz");
////        URLConnection connection = url.openConnection();
////        return null;
////    }
//}
