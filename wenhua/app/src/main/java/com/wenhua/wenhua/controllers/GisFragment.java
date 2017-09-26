package com.wenhua.wenhua.controllers;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.bumptech.glide.Glide;
import com.wenhua.wenhua.R;
import com.wenhua.wenhua.WhMapMarkerInfo;
import com.wenhua.wenhua.constants.Constants;
import com.wenhua.wenhua.controllers.base.BaseFragment;
import com.wenhua.wenhua.http.DefaultStringCallBack;
import com.wenhua.wenhua.utils.ACache;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by castiel on 2016/8/4.
 */
public class GisFragment extends BaseFragment {
    private MapView mMapView;
    private LocationClient locationClient;
    private BaiduMap baiduMap;
    private boolean firstLocation;
    private BitmapDescriptor mCurrentMarker;
    private RelativeLayout mMarkerInfoLy;
    //The following is added by Bill Chen 20160923
    private BitmapDescriptor myMarker;
    private Button mButSnap;
    private Button mButVid;
    private Button mButWechat;
    //private MyLocationConfigeration config;
    private String strCameraId;
    private String strPhoneNumber=null;

    //The following is added by Bill Chen
    private LocationManager locationManager;
    private String provider;
    double dbMobilePhoneLat;
    double dbMobilePhoneLng;
    private int nMobilePhoneInit;
    private Location locationPhone;


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
       // SDKInitializer.initialize(getApplicationContext());
        Context aa=getApplicationContext();
        SDKInitializer.initialize(aa);
        setContentView(R.layout.fragment_gis);
        mMapView=(MapView)findViewById(R.id.map_view);
        mMarkerInfoLy = (RelativeLayout) findViewById(R.id.id_marker_info);
        //The following is added by Bill Chen 20160920
        baiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15f);
        baiduMap.setMapStatus(msu);

        //The following is added by Bill Chen 20160823 to add button key
        mButSnap=(Button)findViewById(R.id.but_snap);
        mButVid =(Button)findViewById(R.id.but_vid);
        mButWechat =(Button)findViewById(R.id.but_wechat);
        mButSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.wh_token_sendingcmd), false);
                String params = "CmdId=300&";
                params += "CamId=" + strCameraId+"&" ;
                params += "NumId=" + "0000";
                OkHttpUtils.postString()
                        //.url("http://" + ACache.get(getActivity()).getAsString(Constants.ACACHE_IP) + "/wechatv2/CamAction") //http://120.76.203.17/wechatv2/CamListJson)
                        .url("http://" + ACache.get(getActivity()).getAsString(Constants.ACACHE_IP) + "/CamAction") //http://120.76.203.17/wechatv2/CamListJson)
                        .content(params)
                        .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                        .tag(getActivity())
                        .build()
                        .execute(new DefaultStringCallBack(getActivity()) {

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                super.onError(call, e, id);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                super.onResponse(response, id);
                                progressDialog.dismiss();
                            }
                        });
            }
        });
        //
        mButVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.wh_token_sendingcmd), false);
                String params = "CmdId=400&";
                params += "CamId=" + strCameraId +"&" ;
                params += "NumId=" + "0000";
                OkHttpUtils.postString()
                        //.url("http://" + ACache.get(getActivity()).getAsString(Constants.ACACHE_IP) + "/wechatv2/CamAction") //http://120.76.203.17/wechatv2/CamListJson)
                        .url("http://" + ACache.get(getActivity()).getAsString(Constants.ACACHE_IP) + "/CamAction") //http://120.76.203.17/wechatv2/CamListJson)
                        .content(params)
                        .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                        .tag(getActivity())
                        .build()
                        .execute(new DefaultStringCallBack(getActivity()) {

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                super.onError(call, e, id);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                super.onResponse(response, id);
                                progressDialog.dismiss();
                            }
                        });
            }
        });
        //
        mButWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.wh_token_sendingcmd),false);
                String params = "camId=" +strCameraId +"&";
                params += "type=0";
                OkHttpUtils.postString()
                        //.url("http://"+ ACache.get(getActivity()).getAsString(Constants.ACACHE_IP)+"/wechatv2/WechatPush") //http://120.76.203.17/wechatv2/CamListJson)
                        .url("http://"+ ACache.get(getActivity()).getAsString(Constants.ACACHE_IP)+"/WechatPush") //http://120.76.203.17/wechatv2/CamListJson)
                        .content(params)
                        .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                        .tag(getActivity())
                        .build()
                        .execute(new DefaultStringCallBack(getActivity()){
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                super.onError(call, e, id);
                                progressDialog.dismiss();
                            }
                            @Override
                            public void onResponse(String response, int id) {
                                super.onResponse(response, id);
                                progressDialog.dismiss();
                            }
                        });
            }
        });
        //The above is added by Bill Chen for button press option

        //location initialization
        locationClient = new LocationClient(getActivity());
        firstLocation =true;

        //Set the correlevant location informations
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.disableCache(true);
        option.setCoorType("bd09ll");//set the cooperation type
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
        //setup the user self-defined icon
        myMarker = BitmapDescriptorFactory.fromResource(R.drawable.camera1);
        //

        //The following is added by Bill Chen 20160826 for mobile phone GPS
        nMobilePhoneInit=0;
        locationManager=(LocationManager)(getActivity().getSystemService(Context.LOCATION_SERVICE)) ;
        List<String> providerList=locationManager.getProviders(true);
        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;
        }
        else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider=LocationManager.NETWORK_PROVIDER;
        }
        else
        {
            Toast.makeText(getActivity(), "请提供位置服务", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            //locationPhone = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationPhone = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            //dialogGPS(this.getContext()); // lets the user know there is a problem with the gps
            Toast.makeText(getActivity(), "请提供位置服务安全许可", Toast.LENGTH_SHORT).show();
            return;
        }
        if(locationPhone==null)
        {
            Toast.makeText(getActivity(), "不能获取到手机GPS坐标", Toast.LENGTH_SHORT).show();
        }
        else {
            nMobilePhoneInit=1;
            dbMobilePhoneLat = locationPhone.getLatitude();
            dbMobilePhoneLng = locationPhone.getLongitude();
        }

        //The above is added by Bill Chen 20160826 for mobile phone GPS
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // map view 销毁后不在处理新接收的位置
                if (location == null || mMapView == null)
                    return;
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                // 设置定位数据
                baiduMap.setMyLocationData(locData);

                // 第一次定位时，将地图位置移动到当前位置
                if (firstLocation)
                {
                    firstLocation = false;
                    LatLng xy = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(xy);
                    baiduMap.animateMapStatus(status);
/*
                    //The following will show the Marker and infoWIndows
                    //准备 marker option 添加 marker 使用
                    OverlayOptions markerOptions = new MarkerOptions().icon(myMarker).position(xy);
                    //获取添加的 marker 这样便于后续的操作
                    Marker marker = (Marker) baiduMap.addOverlay(markerOptions);
                    //The following is added by Bill Che tempory
                    WhMapMarkerInfo info=new WhMapMarkerInfo(location.getLatitude(),location.getLongitude(),0,"wh","100",0);
                    //The above is added by Bill Chen tempory
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info", info);
                    marker.setExtraInfo(bundle);
*/
                    //The following is added by Bill Chen 2 send latitude and longitude
                    double fLat=location.getLatitude();
                    double fLng=location.getLongitude();
                    if(nMobilePhoneInit==1)
                    {
                        fLat=dbMobilePhoneLat;
                        fLng=dbMobilePhoneLng;
                    }
                    String strlat=Double.toString(fLat);
                    String strlng=Double.toString(fLng);
                    String params = "lat=" +strlat +"&";
                    params += "lng=" + strlng;
                    OkHttpUtils.postString()
                            //.url("http://" + ACache.get(getActivity()).getAsString(Constants.ACACHE_IP) + "/wechatv2/FeedCambyLoc") //http://120.76.203.17/wechatv2/CamListJson)
                            .url("http://" + ACache.get(getActivity()).getAsString(Constants.ACACHE_IP) + "/FeedCambyLoc") //http://120.76.203.17/wechatv2/CamListJson)
                            .content(params)
                            .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                            .tag(getActivity())
                            .build()
                            .execute(new DefaultStringCallBack(getActivity()) {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    super.onError(call, e, id);
                                   // progressDialog.dismiss();
                                }
                                @Override
                                public void onResponse(String response, int id) {
                                    super.onResponse(response, id);
                                    //progressDialog.dismiss();
                                    String[] tmp=response.split(",");
                                    String camID=tmp[0];
                                    String strLat=tmp[1];
                                    String strLnt=tmp[2];
                                    strCameraId=camID;
                                    //
                                    //The following will show the Marker and infoWIndows
                                    //准备 marker option 添加 marker 使用
                                    double lat0=Double.parseDouble(strLat);
                                    double lnt0=Double.parseDouble(strLnt);
                                    //LatLng xy = new LatLng(location.getLatitude(),location.getLongitude());
                                    LatLng xy0 = new LatLng(lat0,lnt0);

                                    //The following is added by Bill Chen for gps2baidu
                                    CoordinateConverter converter  = new CoordinateConverter();
                                    converter.from(CoordinateConverter.CoordType.GPS);
                                    converter.coord(xy0);
                                    LatLng xy = converter.convert();
                                    double lat=xy.latitude;
                                    double lnt=xy.latitude;
                                    //The above is added by Bill Chen for GPS2Baidu

                                    OverlayOptions markerOptions = new MarkerOptions().icon(myMarker).position(xy);
                                    //获取添加的 marker 这样便于后续的操作
                                    Marker marker = (Marker) baiduMap.addOverlay(markerOptions);
                                    //The following is added by Bill Che tempory
                                    //WhMapMarkerInfo info=new WhMapMarkerInfo(location.getLatitude(),location.getLongitude(),0,"wh","100",0);
                                   // WhMapMarkerInfo info=new WhMapMarkerInfo(lat,lnt,0,"wh","100",0);
                                    WhMapMarkerInfo info=new WhMapMarkerInfo(lat,lnt,camID);
                                    //The above is added by Bill Chen tempory
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("info", info);
                                    marker.setExtraInfo(bundle);
                                    //
                                }
                            });


                    //The above is to gain the remote camera's latitude and longitude

                }
            }
        });
        //The following will start it
        locationClient.start();
        //The following is added by Bill Chen for the location request
        if (locationClient != null && locationClient.isStarted())
            locationClient.requestLocation();
        else
            Log.d("Bill Chen", "locationClient is null or not started");
        //
       whInitMarkerClick();
       whInitMapClick();
    }

    //The following is added by Bill Chen for Marker Client Event
    private void whInitMarkerClick()
    {
        // 对Marker的点击
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(final Marker marker)
            {
                //The following is added by Bill Chen to do marker pointer process
                WhMapMarkerInfo info = (WhMapMarkerInfo) marker.getExtraInfo().get("info");
/*
                InfoWindow mInfoWindow;
                // 生成一个TextView用户在地图中显示InfoWindow
                TextView location = new TextView(getApplicationContext());
                location.setBackgroundResource(R.drawable.location_tips);
                location.setPadding(30, 20, 30, 50);
                location.setText(info.getName());
                // 将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                Point p = baiduMap.getProjection().toScreenLocation(ll);
                p.y -= 47;
                LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
                //
                mInfoWindow = new InfoWindow(location, llInfo, -47);
                baiduMap.showInfoWindow(mInfoWindow);
                // 设置详细信息布局为可见
                mMarkerInfoLy.setVisibility(View.VISIBLE);
                // 根据商家信息为详细信息布局设置信息
               popupInfo(mMarkerInfoLy, info);
*/
                // 设置详细信息布局为可见
                mMarkerInfoLy.setVisibility(View.VISIBLE);
                //The following is added by Bill Chen 20160923 for image show
                String strCameraId=info.getCamId();
                int value=(int)(Math.random()*1000);
                String dumId=value+".jpg";
                //String strRealLiveImage="http://" + ACache.get(getActivity()).getAsString(Constants.ACACHE_IP) + "/wechatv2/LiveImageShow?camId="+strCameraId.trim()+"&dumId="+dumId;
                //String strRealLiveImage="http://" + ACache.get(getActivity()).getAsString(Constants.ACACHE_IP) + "/LiveImageShow?camId="+strCameraId.trim()+"&dumId="+dumId;
                String strRealLiveImage="http://" + ACache.get(getActivity()).getAsString(Constants.ACACHE_IP) + "/AppLiveImageShow?camId="+strCameraId.trim()+"&dumId="+dumId;
                Glide.with(getActivity())
                        .load(strRealLiveImage)
                        .crossFade()
                        .into((ImageView) findViewById(R.id.info_img));
                //.placeholder(R.drawable.placeholder)
                //The above is added by Bill Chen 20160923 for image show
                return true;
            }
        });
    }

    //The following is added by Bill Chen for MapClickEvent
    private void whInitMapClick()
    {
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener()
        {

            @Override
            public boolean onMapPoiClick(MapPoi arg0)
            {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0)
            {
                mMarkerInfoLy.setVisibility(View.GONE);
                baiduMap.hideInfoWindow();

            }
        });
    }
    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

}



