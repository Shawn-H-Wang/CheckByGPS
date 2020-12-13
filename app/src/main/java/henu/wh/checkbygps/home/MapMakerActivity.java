package henu.wh.checkbygps.home;

import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import henu.wh.checkbygps.R;
import henu.wh.checkbygps.role.MemSignInfo;

public class MapMakerActivity extends BaseActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private MyLocationData locData;//定位坐标
    private static MemSignInfo memSignInfo;

    public static void setMemSignInfo(MemSignInfo memSignInfo) {
        MapMakerActivity.memSignInfo = memSignInfo;
    }

    @Override
    protected int getConentView() {
        SDKInitializer.initialize(getApplicationContext());//百度地图
        // 注册 SDK 广播监听者
        RegisterBroadcast();
        return R.layout.activity_map_maker;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        LatLng point = new LatLng(Double.parseDouble(memSignInfo.getWeidu()),
                Double.parseDouble(memSignInfo.getJingdu()));
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(15)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        mBaiduMap.addOverlay(option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

}