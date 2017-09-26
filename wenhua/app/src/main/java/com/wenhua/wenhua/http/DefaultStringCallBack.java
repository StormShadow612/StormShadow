package com.wenhua.wenhua.http;

import android.content.Context;

import com.wenhua.wenhua.utils.ToastUtils;
import com.wenhua.wenhua.utils.log.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by castiel on 2016/8/8.
 */

public class DefaultStringCallBack extends StringCallback {

    private Context mContext;

    public DefaultStringCallBack(Context context){
        this.mContext = context;
    }

//    @Override
//    public String parseNetworkResponse(Response response, int id) throws IOException {
//        LogUtils.d(response);
//        return new String(response.body().bytes(),"utf-8");
//    }

    @Override
    public void onError(Call call, Exception e, int id) {
        ToastUtils.show(mContext,"网络加载失败");
        LogUtils.d("id:"+id+" e:"+e);
    }

    @Override
    public void onResponse(String response, int id) {
        LogUtils.d(response);
    }
}
