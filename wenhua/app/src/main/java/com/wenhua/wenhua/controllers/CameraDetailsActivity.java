package com.wenhua.wenhua.controllers;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wenhua.wenhua.R;
import com.wenhua.wenhua.constants.Constants;
import com.wenhua.wenhua.http.DefaultStringCallBack;
import com.wenhua.wenhua.utils.ACache;
import com.wenhua.wenhua.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;
import okhttp3.MediaType;

public class CameraDetailsActivity extends AppCompatActivity {
    public String strCameraId;
    public String strPhoneNumber;
    public String strCameraLine;
    public String strCameraTower;
    private int nFileExist;

    //The following is added by Bill Chen 20170717
    private Toolbar toolbar;

    //The following is added by Bill Chen 20170719
    private uk.co.senab.photoview.PhotoView mPhotoview;

    //The following is added by Bill CHen 20170717
    private Handler handler=new Handler(){
      public void handleMessage(Message msg){
          switch(msg.what)
          {
              case 1:
              {
                  int value=(int)(Math.random()*1000);
                  String dumId=value+".jpg";
                  // String strRealLiveImage="http://" + ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP) + "/wechatv2/LiveImageShow?camId="+strCameraId+"&dumid="+dumId;
                  //String strRealLiveImage="http://" + ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP) + "/LiveImageShow?camId="+strCameraId+"&dumid="+dumId;
                  String strRealLiveImage="http://" + ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP) + "/AppLiveImageShow?camId="+strCameraId+"&dumid="+dumId;
                  Glide.with(CameraDetailsActivity.this)
                          .load(strRealLiveImage)
                          .crossFade()
                          .placeholder(R.drawable.placeholder)
                          //.into((ImageView) findViewById(R.id.imageView));
                          //.into((uk.co.senab.photoview.PhotoView) findViewById(R.id.imageView));
                          .into(mPhotoview);
                  break;
              }
              case 2:
              {
                  //showToast("拍照指令下发失败！！");
                  //final ProgressDialog progressDialog = ProgressDialog.show(CameraDetailsActivity.this, "", "拍照指令下发失败！！", false);
                  Toast.makeText(CameraDetailsActivity.this, "拍照指令下发失败！！", Toast.LENGTH_SHORT).show();
                  break;
              }
              default:
                  break;
          }
      }
    };

    @Override
    public void onConfigurationChanged(Configuration config){
        super.onConfigurationChanged(config);
        if (this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) {
        }else if (this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_details);

        //
        mPhotoview= (uk.co.senab.photoview.PhotoView)findViewById(R.id.imageView);
        //The following is added by Bill Chen
        Intent intent=getIntent();
        String tips=intent.getStringExtra("tips");
        String strLine=intent.getStringExtra("line");
        String strTower=intent.getStringExtra("tower");
        nFileExist=0;

        //设备标识:  WH10412160502W14 号码:  13537894557 地址: 枣庄
        String strAdrToken="地址:";
        String strPhoneToken="号码:";
        String strDevToken="设备标识:";
        int nAdrIndex=tips.indexOf(strAdrToken);
        int nPhoneIndex=tips.indexOf(strPhoneToken);
        int nDevIndex=tips.indexOf(strDevToken);

        String strID=tips.substring(nDevIndex+strDevToken.length(), nPhoneIndex-1);
        String strPhone =tips.substring(nPhoneIndex+strPhoneToken.length(), nAdrIndex-1);

        //
        strCameraId=strID.trim();
        strPhoneNumber=strPhone.trim();
        strCameraLine=strLine.trim();
        strCameraTower=strTower.trim();


        String strToshow="线路:"+strCameraLine +" 塔号:"+strCameraTower;

        //The following is added by Bill Chen 20170717
        //setTitle(strToshow);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(strToshow);
        setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(R.mipmap.ic_menu_white_24dp);

        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationIcon(R.mipmap.baturn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Snackbar.make(toolbar, "Click setNavigationIcon", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        });
        //toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        //String aa="https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1717816186,2947076118&fm=21&gp=0.jpg";
        int value=(int)(Math.random()*1000);
        String dumId=value+".jpg";
       // String strRealLiveImage="http://" + ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP) + "/wechatv2/LiveImageShow?camId="+strCameraId+"&dumid="+dumId;
      //  String strRealLiveImage="http://" + ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP) + "/LiveImageShow?camId="+strCameraId+"&dumid="+dumId;
        String strRealLiveImage="http://" + ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP) + "/AppLiveImageShow?camId="+strCameraId+"&dumid="+dumId;
        Glide.with(CameraDetailsActivity.this)
                .load(strRealLiveImage)
                .crossFade()
                .placeholder(R.drawable.placeholder)
                //.into((ImageView) findViewById(R.id.imageView));
                //.into((uk.co.senab.photoview.PhotoView) findViewById(R.id.imageView));
                .into(mPhotoview);
        //The above is added by Bill Chen
       // mPhotoview.setRotation(90);
        //The following is commented by Bill Chen 20170719
        /*
        //The following is for the float button to close the active and return the former activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();
                finish();
            }
        });
        */
        //The above is commented by Bill Chen 20170719
    }
    //the above is the oncreate function
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_cam_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.wh_action_snapshot: {
                //Toast.makeText(CameraDetailsActivity.this, strCameraId, Toast.LENGTH_SHORT).show();
                final ProgressDialog progressDialog = ProgressDialog.show(CameraDetailsActivity.this, "", getString(R.string.wh_token_sendingcmd), false);
                String params = "CmdId=100&";
                params += "CamId=" + strCameraId + "&";
                params += "NumId=" + strPhoneNumber;
                OkHttpUtils.postString()
                        // .url("http://"+ ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP)+"/wechatv2/CamAction") //http://120.76.203.17/wechatv2/CamListJson)
                        .url("http://" + ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP) + "/AppCamAction") //http://120.76.203.17/wechatv2/CamListJson)
                        .content(params)
                        .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                        .tag(CameraDetailsActivity.this)
                        .build()
                        .execute(new DefaultStringCallBack(CameraDetailsActivity.this) {

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
                showToast("拍照指令正在下发......");
                //The following is added by Bill Chen 20170719 to deal with the feedback of the server to this snapshot command
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //0.the following to
                        //Boolean tmpLoopCtl=true;
                        long sleepTime=10000;
                        int tmpTryCount=0;
                        long tmpTryrst=0;
                        final boolean rs[]={false};
                        final int feedback[]={0};
                        while(true)
                        {
                            //a. The following to sleep some time first
                            tmpTryrst=sleepTime -(tmpTryCount++)*3000;
                            if(tmpTryrst>0)
                            {
                                sleepTime=tmpTryrst;
                            }
                            else
                            {
                                sleepTime=2000;
                            }
                            try{
                                Thread.sleep(sleepTime);
                            }
                           catch(InterruptedException e)
                           {
                               return;
                           }

                            //b. The following to send check command to server
                            String params = "CmdId=100&";
                            params += "CamId=" + strCameraId;
                            OkHttpUtils.postString()
                                    // .url("http://"+ ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP)+"/wechatv2/CamAction") //http://120.76.203.17/wechatv2/CamListJson)
                                    .url("http://" + ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP) + "/AppCheckCam") //http://120.76.203.17/wechatv2/CamListJson)
                                    .content(params)
                                    .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                                    .tag(CameraDetailsActivity.this)
                                    .build()
                                    .execute(new DefaultStringCallBack(CameraDetailsActivity.this) {

                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            super.onError(call, e, id);
                                            //progressDialog.dismiss();
                                        }
                                        @Override
                                        public void onResponse(String response, int id) {
                                            super.onResponse(response, id);
                                            //progressDialog.dismiss();
                                            String  strresult = (String) JsonUtils.getInstance().getJsonValue(response,"Feedback");
                                            int result = Integer.parseInt(strresult);
                                            if(result==2)
                                            {
                                                feedback[0]=2;
                                                rs[0]=true;
                                            }
                                            else
                                                if(result==1)
                                                {
                                                    feedback[0]=1;
                                                    rs[0]=true;
                                                }

                                        }
                                    });
                            if(rs[0]==true)
                                break;
                        }
                        //1.
                        Message message = Message.obtain(); //构造器（但是获取Message对象的最好方法是调用Message.obtain()）
                        message.what = feedback[0];
                        handler.sendMessage(message);
                    }
                }).start();
                //The above is added by Bill Chen to deal with the feedback of the server to this snapshot command
                break;
            }
            case R.id.wh_action_video: {
                final ProgressDialog progressDialog = ProgressDialog.show(CameraDetailsActivity.this, "", getString(R.string.wh_token_sendingcmd), false);
                String params = "CmdId=200&";
                params += "CamId=" + strCameraId + "&";
                params += "NumId=" + strPhoneNumber;
                OkHttpUtils.postString()
                        //.url("http://" + ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP) + "/wechatv2/CamAction") //http://120.76.203.17/wechatv2/CamListJson)
                        .url("http://" + ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP) + "/AppCamAction") //http://120.76.203.17/wechatv2/CamListJson)
                        .content(params)
                        .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                        .tag(CameraDetailsActivity.this)
                        .build()
                        .execute(new DefaultStringCallBack(CameraDetailsActivity.this) {

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
                showToast("指令已下发");
                break;
            }
            case R.id.wh_action_wechat_push: {
                final ProgressDialog progressDialog = ProgressDialog.show(CameraDetailsActivity.this, "", getString(R.string.wh_token_sendingcmd),false);
                String params = "camId=" +strCameraId +"&";
                params += "type=0";
                OkHttpUtils.postString()
                        // .url("http://"+ ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP)+"/wechatv2/WechatPush") //http://120.76.203.17/wechatv2/CamListJson)
                        .url("http://"+ ACache.get(CameraDetailsActivity.this).getAsString(Constants.ACACHE_IP)+"/WechatPush") //http://120.76.203.17/wechatv2/CamListJson)
                        .content(params)
                        .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                        .tag(CameraDetailsActivity.this)
                        .build()
                        .execute(new DefaultStringCallBack(CameraDetailsActivity.this){

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
                showToast("指令已下发");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}
