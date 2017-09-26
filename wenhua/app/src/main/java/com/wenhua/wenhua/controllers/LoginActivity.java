package com.wenhua.wenhua.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wenhua.wenhua.R;
import com.wenhua.wenhua.constants.Constants;
import com.wenhua.wenhua.controllers.base.BaseActivity;
import com.wenhua.wenhua.http.DefaultStringCallBack;
import com.wenhua.wenhua.utils.ACache;
import com.wenhua.wenhua.utils.JsonUtils;
import com.wenhua.wenhua.utils.ToastUtils;
import com.wenhua.wenhua.utils.log.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * Created by castiel on 2016/8/6.
 * 登录
 */
public class LoginActivity extends BaseActivity {

    private EditText mEtUserName;
    private EditText mEtPassword;
    private EditText mEtIp;
    private Button  mBtnLogin;
    private ImageView mLoginImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //The following is added by Bill Chen 20170717
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        //The above is added by Bill Chen 20170717
        setContentView(R.layout.activity_login);

        initView();
        initListener();
    }


    @Override
    public void initView() {
        mEtUserName = (EditText) findViewById(R.id.login_et_username);
        mEtPassword = (EditText) findViewById(R.id.login_et_password);
        mEtIp = (EditText) findViewById(R.id.login_et_ip);
        mBtnLogin = (Button) findViewById(R.id.login_btn);

        String ip = ACache.get(LoginActivity.this).getAsString(Constants.ACACHE_IP);
        String ukey= ACache.get(LoginActivity.this).getAsString("username_key");
        String pkey=ACache.get(LoginActivity.this).getAsString("password_key");
        if(!TextUtils.isEmpty(ip)){
            mEtIp.setText(ip);
            mEtIp.setSelection(ip.length());
        }
        if(!TextUtils.isEmpty(ukey)){
            mEtUserName.setText(ukey);
            mEtUserName.setSelection(ukey.length());
        }
        if(!TextUtils.isEmpty(pkey)){
            mEtPassword.setText(pkey);
            mEtPassword.setSelection(pkey.length());
        }



    }

    @Override
    public void initListener() {
        mBtnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        mEtUserName.setError(null);
        mEtPassword.setError(null);
        mEtIp.setError(null);

        String ip = mEtIp.getText().toString();
        String username = mEtUserName.getText().toString();
        String password = mEtPassword.getText().toString();

        ACache.get(LoginActivity.this).put("username_key",username,5*ACache.TIME_DAY);
        ACache.get(LoginActivity.this).put("password_key",password,5*ACache.TIME_DAY);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mEtPassword.setError("密码不能为空");
            focusView = mEtPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            mEtUserName.setError("用户名不能为空");
            focusView = mEtUserName;
            cancel = true;
        }

        if(TextUtils.isEmpty(ip)){
            mEtIp.setError("ip不能为空");
            focusView = mEtIp;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            final ProgressDialog progressDialog =  ProgressDialog.show(this,"","正在登录...",true,true);

            //"http://120.76.203.17/wechatv2/UserLogin?username=admin&pwd=admin"
            OkHttpUtils.post()
                    //.url("http://"+ip+"/wechatv2/UserLogin")
                    .url("http://"+ip+"/UserLogin")
                    .addParams("username",username)
                    .addParams("pwd",password)
                    .tag(this)
                    .build()
                    .execute(new DefaultStringCallBack(LoginActivity.this) {

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            super.onError(call, e, id);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.d(response);
                            ACache.get(LoginActivity.this).put(Constants.ACACHE_IP,mEtIp.getText().toString());
                            String userStatus = (String) JsonUtils.getInstance().getJsonValue(response,"nUserStatus");
                            progressDialog.dismiss();

                            if("1".equals(userStatus)){
                                String token = (String) JsonUtils.getInstance().getJsonValue(response,"sToken");

                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("token",token);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }else{
                                ToastUtils.show(LoginActivity.this,"用户名或密码错误，请重新登录");
                            }
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}

