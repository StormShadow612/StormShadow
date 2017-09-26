package com.wenhua.wenhua.controllers.base;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.wenhua.wenhua.http.OkHttpUrlLoader;
import com.wenhua.wenhua.utils.log.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by castiel on 2016/8/4.
 */


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.configAllowLog = true;
        initGlide();
        initOkhttp();
    }

    /**
     * glide网络层修改为okhttp
     */
    private void initGlide(){
        OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
        Glide.get(getApplicationContext()).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }

    private void initOkhttp(){
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("WenHua")) //为网络请求添加log日志
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //访问所有http https网站
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
