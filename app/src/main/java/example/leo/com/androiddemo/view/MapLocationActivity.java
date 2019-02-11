package example.leo.com.androiddemo.view;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import example.leo.com.androiddemo.R;
import example.leo.com.androiddemo.overlay.PoiOverlay;
import example.leo.com.androiddemo.utils.AMapUtil;
import example.leo.com.androiddemo.utils.ToastUtil;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.view
 * @ClassName: MapLocationActivity
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/1/29 10:04
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/29 10:04
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MapLocationActivity extends FragmentActivity implements
        AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, TextWatcher,
        PoiSearch.OnPoiSearchListener, View.OnClickListener, Inputtips.InputtipsListener {
    private AMap aMap;
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private EditText editCity;// 要输入的城市名字或者城市区号
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maplocation);
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            setUpMap();
        }
    }

    /**
     * 设置页面监听
     */
    private void setUpMap() {
        Button searButton = (Button) findViewById(R.id.searchButton);
        searButton.setOnClickListener(this);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
        searchText.addTextChangedListener(this);// 添加文本输入框监听事件
        editCity = (EditText) findViewById(R.id.city);
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    /**
     * 点击搜索按钮
     */
    public void searchButton() {
        keyWord = AMapUtil.checkEditText(searchText);
        if ("".equals(keyWord)) {
            ToastUtil.show(MapLocationActivity.this, "请输入搜索关键字");
            return;
        } else {
            doSearchQuery();
            aMap.setMyLocationEnabled(false);
        }
    }

    /**
     * 点击下一页按钮
     */
    public void nextButton() {
        if (query != null && poiSearch != null && poiResult != null) {
            if (poiResult.getPageCount() - 1 > currentPage) {
                currentPage++;
                query.setPageNum(currentPage);// 设置查后一页
                poiSearch.searchPOIAsyn();
            } else {
                ToastUtil.show(MapLocationActivity.this,
                        R.string.no_result);
            }
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", editCity.getText().toString());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
                null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAMapNavi(marker);
            }
        });
        return view;
    }

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {

            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());

        }

    }

    /**
     * 判断高德地图app是否已经安装
     */
    public boolean getAppIn() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(
                    "com.autonavi.minimap", 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        // 本手机没有安装高德地图app
        if (packageInfo != null) {
            return true;
        }
        // 本手机成功安装有高德地图app
        else {
            return false;
        }
    }

    /**
     * 获取当前app的应用名字
     */
    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager
                .getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(MapLocationActivity.this, infomation);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, editCity.getText().toString());
            Inputtips inputTips = new Inputtips(MapLocationActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }


    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtil.show(MapLocationActivity.this,
                                R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(MapLocationActivity.this,
                        R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }

    /**
     * Button点击事件回调方法
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 点击搜索按钮
             */
            case R.id.searchButton:
                searchButton();
                break;
            /**
             * 点击下一页按钮
             */
            case R.id.nextButton:
                nextButton();
                break;
            default:
                break;
        }
    }


    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }
}