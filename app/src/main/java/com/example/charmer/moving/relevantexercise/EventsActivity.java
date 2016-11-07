package com.example.charmer.moving.relevantexercise;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.VisibleRegion;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.charmer.moving.MyApplicition.MyApplication;
import com.example.charmer.moving.R;

public class EventsActivity extends AppCompatActivity implements AMap.OnMapClickListener,LocationSource,AMapLocationListener,
        AMap.OnMapLongClickListener, AMap.OnCameraChangeListener,AMap.OnMapTouchListener,GeocodeSearch.OnGeocodeSearchListener {

    private AMap aMap;
    private AMapLocationClient mlocationClient;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;
    private RelativeLayout finishthis;
//    private TextView mTapTextView;
//
//    private TextView mCameraTextView;
//
//    private TextView mTouchTextView;

    private TextView destinationView;
    private String destination; //目的地的字符串
    private Button Determine;
    double latitude;
    double longitude;
    private String city;
    private GeocodeSearch geocoderSearch; //地理编码


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_events);

        mapView = (MapView) findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);// 此方法必须重写


        //地理编码事件监听

        geocoderSearch = new GeocodeSearch(this);

        geocoderSearch.setOnGeocodeSearchListener(this);

        init();

        Determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(destination!=null&&!"".equals(destination)) {
                    Intent intent = getIntent();
                    intent.putExtra("str1",destination);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(EventsActivity.this,"请选择地点",Toast.LENGTH_SHORT).show();
                }
            }
        });

        finishthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 初始化AMap对象
     */

    private void init() {

        if (aMap == null) {

            aMap = mapView.getMap();

            setUpMap();

        }

//        mTapTextView = (TextView) findViewById(R.id.tap_text);
//
//        mCameraTextView = (TextView) findViewById(R.id.camera_text);
//
//        mTouchTextView = (TextView) findViewById(R.id.touch_text);
        finishthis = (RelativeLayout)findViewById(R.id.finishthis);
        Determine =(Button)findViewById(R.id.Determine);
        destinationView = (TextView) findViewById(R.id.destinationView);

    }


    /**
     * amap添加一些事件监听器
     */

    private void setUpMap() {

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.poi_marker_pressed));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//        getMap().setLatLonQuanVisible(false);
        aMap.getUiSettings().setCompassEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //

        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器

        //aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器

        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器

        aMap.setOnMapTouchListener(this);// 对amap添加触摸地图事件监听器

    }


    /**
     * 方法必须重写
     */

    @Override

    protected void onResume() {

        super.onResume();

        mapView.onResume();

    }


    /**
     * 方法必须重写
     */

    @Override

    protected void onPause() {

        super.onPause();

        mapView.onPause();

    }


    /**
     * 方法必须重写
     */

    @Override

    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        mapView.onSaveInstanceState(outState);

    }


    /**
     * 方法必须重写
     */

    @Override

    protected void onDestroy() {

        super.onDestroy();

        mapView.onDestroy();

    }


    /**
     * 对单击地图事件回调
     */

    @Override

    public void onMapClick(LatLng point) {

//        mTapTextView.setText("tapped, point=" + point);

        RegeocodeQuery query = new RegeocodeQuery(convertToLatLng(point), 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    public static LatLonPoint convertToLatLng(LatLng point) {
        return new LatLonPoint(point.latitude, point.longitude);
    }

    /**
     * 对长按地图事件回调
     */

    @Override

    public void onMapLongClick(LatLng point) {

//        mTapTextView.setText("long pressed, point=" + point);

    }


    /**
     * 对正在移动地图事件回调
     */

    @Override

    public void onCameraChange(CameraPosition cameraPosition) {

//        mCameraTextView.setText("onCameraChange:" + cameraPosition.toString());

    }


    /**
     * 对移动地图结束事件回调
     */

    @Override

    public void onCameraChangeFinish(CameraPosition cameraPosition) {

//        mCameraTextView.setText("onCameraChangeFinish:"
//
//                + cameraPosition.toString());

        VisibleRegion visibleRegion = aMap.getProjection().getVisibleRegion(); // 获取可视区域、


        LatLngBounds latLngBounds = visibleRegion.latLngBounds;// 获取可视区域的Bounds

//        boolean isContain = latLngBounds.contains();// 判断上海经纬度是否包括在当前地图可见区域
//
//        if (isContain) {
//
//            ToastUtil.show(EventsActivity.this, "上海市在地图当前可见区域内");
//
//        } else {
//
//            ToastUtil.show(EventsActivity.this, "上海市超出地图当前可见区域");
//
//        }

    }

    /**
     * 对触摸地图事件回调
     */

    @Override

    public void onTouch(MotionEvent event) {


//        mTouchTextView.setText("触摸事件：屏幕位置"+event.getY()+" "+event.getY());

    }
    //地理逆编码回调函数

    @Override

    public void onGeocodeSearched(GeocodeResult arg0, int arg1) {

// TODO Auto-generated method stub


    }

//地理编码回调函数

    @Override

    public void onRegeocodeSearched(RegeocodeResult result, int resultcode) {

// TODO Auto-generated method stub

//        if(resultcode == 0)
//
//        {
//
//            if(result != null && result.getRegeocodeAddress() != null
//
//                    && result.getRegeocodeAddress().getFormatAddress() != null)
//
//            {
//
//                destination = result.getRegeocodeAddress().getFormatAddress();         //获得目的地名称
//                destinationView.setText(destination);
//                System.out.println(destination);
//            }
//
//        }

        if (resultcode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                destination = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
//				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//						AMapUtil.convertToLatLng(latLonPoint), 15));
                //regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                System.out.println("addressName+++" + destination);
                destinationView.setText(destination);
            } else {
                System.out.println("noresult+++");
            }
        } else {
            System.out.println("errorerror");
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    /*开启定位时*/
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                Log.d("===纬度：",""+amapLocation.getLatitude());
                latitude = amapLocation.getLatitude();
                longitude = amapLocation.getLongitude();
                city = amapLocation.getCity();
                mlocationClient.stopLocation();
//                int select = 1;
//                listView.removeFooterView(listNot);
//                doSearchQuery();
            } else {
                /*没开启定位时*/
//                String notLat = MobclickAgent.getConfigParams(this, "defaultLatitude");
//                String notLog = MobclickAgent.getConfigParams(this, "defaultLongitude");
                LatLng latLng = new LatLng(latitude,longitude);
                aMap.getUiSettings().setCompassEnabled(false);
                aMap.moveCamera(CameraUpdateFactory.zoomTo(25));
                aMap.getUiSettings().setScaleControlsEnabled(true);// 设置比例尺
                MarkerOptions otMarkerOptions = new MarkerOptions();
                otMarkerOptions.position(latLng);
                otMarkerOptions.draggable(true);
                //下面这个是标记上面这个经纬度在地图的位置是
                otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_pressed));
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                aMap.addMarker(otMarkerOptions);
                city = amapLocation.getCity();
                latitude = latLng.latitude;
                longitude = latLng.longitude;
//                select = 1;
//                listView.removeFooterView(listNot);
//                doSearchQuery();
                mlocationClient.stopLocation();
            }
        }
    }
}