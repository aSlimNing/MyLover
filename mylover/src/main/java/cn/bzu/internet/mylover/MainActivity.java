package cn.bzu.internet.mylover;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author monster
 * @date 2015-10-19
 * @tag:
 *      程序含有一个WebView，一个RepaidFloatingActionButton 一个ToolBar
 */
public class MainActivity extends AppCompatActivity
        implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener,AMapLocationListener{

    private WebView mWebView;
    private ProgressWheel mProgress;

    //TODO 使用的时候替网址和恋人的手机号即可
    private String path = "http://monsterlin.github.io/Lover_Time/";  //网站的链接
    private String mobilePhone = "*********" ;  //恋人手机号

    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;

    private LocationManagerProxy mLocationManagerProxy;

   // private Random mRandom = new Random();

    private String mCity ;  //定位得到当前地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(this);
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mWebView.loadUrl(path);
        WebSettings setting = mWebView.getSettings();
        mWebView.setInitialScale(100); //最小缩放等级 : 25%
        setting.setJavaScriptEnabled(true);
        //支持缩放
        setting.setSupportZoom(true);
        // 缩放按钮
        setting.setBuiltInZoomControls(true);

        mWebView.setWebViewClient(new WebViewClient() {   //设置默认浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //设置进度的反馈
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //网页加载完成
                    mProgress.setVisibility(View.INVISIBLE);
                } else {
                    //加载中
                }
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView(Context context) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MyLover");
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        //TODO 未启用FloatingActionButton
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mWebView = (WebView) findViewById(R.id.wv_web);
        mProgress = (ProgressWheel) findViewById(R.id.progress_wheel);


        /**
         * RapidFloatin
         */
        rfaLayout = (RapidFloatingActionLayout) findViewById(R.id.activity_main_rfal);
        rfaBtn = (RapidFloatingActionButton) findViewById(R.id.activity_main_rfab);

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("给坏蛋打电话")
                        .setResId(R.mipmap.icon_phone)
                        .setIconNormalColor(0xffd84315)
                        .setIconPressedColor(0xffbf360c)
                        .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("给坏蛋发短信")
                        .setResId(R.mipmap.icon_sms)
                        .setIconNormalColor(0xff056f00)
                        .setIconPressedColor(0xff0d5302)
                        .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("我在哪里")
                        .setResId(R.mipmap.icon_location)
                        .setIconNormalColor(0xff056f00)
                        .setIconPressedColor(0xff0d5302)
                        .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                        .setLabel("退出")
                        .setResId(R.mipmap.icon_exit)
                        .setIconNormalColor(0xff283593)
                        .setIconPressedColor(0xff1a237e)
                        .setWrapper(3)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(context, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(context, 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                context,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();

        /**
         * 定位相关设置的策略
         */

        // 初始化定位，只采用网络定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.setGpsEnable(false);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
    }



//TODO 菜单栏未启用
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();  //返回上一页面
                return true;
            } else {
                System.exit(0);  //退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        selectToDo(position);
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        selectToDo(position);
    }

    /**
     * 点击浮动按钮后的操作
     * @param position
     */
    private void selectToDo(int position) {
        switch ( position) {
            case 0:
                new PhoneUtil().call(this,mobilePhone);  //打电话
                rfabHelper.toggleContent();
                break;
            case 1:
                new PhoneUtil().sendSMS(this, mobilePhone.trim()," ");
                rfabHelper.toggleContent();
                break;
            case 2:
                // 注意更换定位时间后，需要先将定位请求删除，再进行定位请求
                getLocation();
                new PhoneUtil().sendSMS(this,mobilePhone.trim(),"亲爱的，我在 ：【"+mCity+"】");
                rfabHelper.toggleContent();
                break;
            case 3:
                finish();
                rfabHelper.toggleContent();
                break;
        }
    }

    /**
     * 得到定位
     */
    private void getLocation() {
        mLocationManagerProxy.removeUpdates(this);
        //int randomTime = mRandom.nextInt(1000);
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 60 * 1000 ,
                15, this);
        mLocationManagerProxy.setGpsEnable(false);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
            //定位回调
        //TODO 定位后的操作
        if(aMapLocation != null &aMapLocation.getAMapException().getErrorCode()==0){
                mCity =  aMapLocation.getAddress();
                 Log.e("City",mCity);
        }else {
            Log.e("AmapErr","Location ERR:" + aMapLocation.getAMapException().getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 移除定位请求
        mLocationManagerProxy.removeUpdates(this);
        // 销毁定位
        mLocationManagerProxy.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
