package tenyond.yank.com.carlocation.main;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsNonRationale;
import com.joker.annotation.PermissionsRationale;
import com.joker.api.Permissions4M;

import de.hdodenhof.circleimageview.CircleImageView;
import tenyond.yank.com.carlocation.R;
import tenyond.yank.com.carlocation.login.LoginActivity;
import tenyond.yank.com.carlocation.utils.DrivingRouteOverlay;
import tenyond.yank.com.carlocation.utils.ViewUtils;

public class MainActivity extends AppCompatActivity {

    //头像
    private CircleImageView head;

    private MapView mapView;
    public LocationClient locationClient = null;
    private MyLocationListener myLocationListener;
    private BaiduMap baiduMap;
    public MyLocationConfiguration.LocationMode currentLocationMode;
    public MyLocationData locData;
    private PoiSearch poiSearch;
    public OnGetPoiSearchResultListener resultListener;
    public Marker markers;
    public RoutePlanSearch routePlanSearch;
    public OnGetRoutePlanResultListener onGetRoutePlanResultListener;
    public LatLng intentPosition;

    public PopupWindow popupWindow;
    private BitmapDescriptor currentMarker;

    public double longitude,latitude;
    private int locationMode;
    public TextView title;
    public Button confirm,cancel;
    public EditText inputEt;

    private ImageButton locateBtn;
    private ImageButton compassBtn;
    private final int LOCATIION_CODE= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initData();
        initView();

        baiduMap.setMyLocationEnabled(true);

        locationClient.start();
        locationClient.requestLocation();

        Permissions4M.get(MainActivity.this)
                .requestOnRationale()
                .requestForce(true)
                .requestUnderM(false)
                .requestPermissions(Manifest.permission_group.LOCATION)
                .requestCodes(LOCATIION_CODE)
                .requestPageType(Permissions4M.PageType.MANAGER_PAGE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(MainActivity.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionsGranted(LOCATIION_CODE)
    public void PermissionsGranted() {
        ViewUtils.showToast(MainActivity.this,"存储权限授权成功");
    }

    @PermissionsDenied(LOCATIION_CODE)
    public void PermissionsDenied() {
        ViewUtils.showToast(MainActivity.this,"存储权限授权失败");
    }

    @PermissionsRationale(LOCATIION_CODE)
    public void PermissionsRationale() {
        ViewUtils.showToast(MainActivity.this,"请开启存储权限授权");
    }

    @PermissionsNonRationale(LOCATIION_CODE)
    public void PermissionsNonRationale(Intent intent){
        startActivity(intent);
    }


    private void initData() {

        routePlanSearch = RoutePlanSearch.newInstance();
        poiSearch = PoiSearch.newInstance();
        currentMarker = BitmapDescriptorFactory.fromResource(R.drawable.location);
        currentLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;

        locationMode = 0;

        final LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd0911");
        option.setScanSpan(500);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(true);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5*60*1000);
        option.setEnableSimulateGps(false);

        locationClient = new LocationClient(getApplicationContext());
        myLocationListener = new MyLocationListener();

        locationClient.setLocOption(option);
        locationClient.registerLocationListener(myLocationListener);

        resultListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    baiduMap.clear();
                    for (PoiInfo poi:result.getAllPoi()) {
                        int i=0;
                        baiduMap.addOverlay(new MarkerOptions().title(String.valueOf(i)).position(poi.location).icon(currentMarker).animateType(MarkerOptions.MarkerAnimateType.grow));
                        Log.e("@@NO_ERROR",poi.location.toString());
                        i++;
                    }

                }

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };

        onGetRoutePlanResultListener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                if (drivingRouteResult == null)
                    return;

                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);//路线覆盖物，MyDrivingRouteOverlay代码下面给出
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };
        routePlanSearch.setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);

    }

    private void planRoute(LatLng fromPosition,LatLng toPosition){
        routePlanSearch.drivingSearch(new DrivingRoutePlanOption()
            .from(PlanNode.withLocation(fromPosition))
            .to(PlanNode.withLocation(toPosition)));
    }


    private void initView() {

        //头像绑定
        head = findViewById(R.id.avater);

        locateBtn = findViewById(R.id.following_btn);
        mapView = findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("@@onMarkerClick",String.valueOf(marker.getTitle()));
                markers = marker;
                intentPosition = marker.getPosition();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        showPopWindow(markers.getTitle());
                    }
                });
                return true;
            }
        });

        inputEt = findViewById(R.id.input_ET);
        compassBtn = findViewById(R.id.compass_btn);
        compassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationMode++;
                switch (locationMode%3){
                    case 0:
                        currentLocationMode = MyLocationConfiguration.LocationMode.COMPASS;
                        break;
                    case 1:
                        currentLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        break;
                    case 2:
                        currentLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
                        break;
                }
                baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(currentLocationMode,true,currentMarker
                        ,0xAAFFFF88,0xAA00FF00));
                Log.e("onClick()",String.valueOf(locationMode));
            }
        });

        locateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,true,currentMarker
                        ,0xAAFFFF88,0xAA00FF00));
                poiSearch.setOnGetPoiSearchResultListener(resultListener);
                if(inputEt.getText().toString().trim().isEmpty()){
                    inputEt.setText("停车场");
                }
                poiSearch.searchNearby(new PoiNearbySearchOption()
                        .keyword(inputEt.getText().toString()).sortType(PoiSortType.distance_from_near_to_far)
                        .location(new LatLng(latitude,longitude)).radius(100000).pageCapacity(20)
                );
            }
        });
        //点击头像跳转到个人信息页面
        head.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,tenyond.yank.com.carlocation.personinfo.PersonInfoActivity.class);
                startActivity(intent);
            }
        });

    }

    private void  showPopWindow(String titles){
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_window,null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        confirm = contentView.findViewById(R.id.confirm);
        cancel = contentView.findViewById(R.id.cancel);
        title = contentView.findViewById(R.id.title);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
                planRoute(new LatLng(latitude,longitude),intentPosition);
                Toast.makeText(MainActivity.this,"You Click Yes",Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        title.setText(titles);
        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onDestroy() {
        locationClient.stop();
        routePlanSearch.destroy();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        poiSearch.destroy();
        super.onDestroy();
    }

    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            longitude = bdLocation.getLongitude();
            latitude = bdLocation.getLatitude();

            locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(100)
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();

            baiduMap.setMyLocationData(locData);

            MyLocationConfiguration configuration = new MyLocationConfiguration(currentLocationMode,true,currentMarker
                                ,0xAAFFFF88,0xAA00FF00);
            baiduMap.setMyLocationConfiguration(configuration);
            Log.e("@@onReceiveLocation()",bdLocation.getLatitude()+"---"+bdLocation.getLongitude());
        }
    }
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            //覆写此方法以改变默认起点图标
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background);
            //这里可以使用BitmapDescriptorFactory.fromView(view)实现自定义view覆盖物，自定义覆盖物请关注以后的文章。
        }

        @Override
        public int getLineColor() {
            // TODO Auto-generated method stub
            //覆写此方法以改变默认绘制颜色
            //Returns:
            //线颜色
            return super.getLineColor();
        }
        @Override
        public BitmapDescriptor getTerminalMarker() {
            //覆写此方法以改变默认终点图标
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background);
        }

        @Override
        public boolean onRouteNodeClick(int i) {
            // TODO Auto-generated method stub
            //覆写此方法以改变默认点击处理
            return true;
        }
    }


}
