package com.wenhua.wenhua.controllers;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.wenhua.wenhua.R;
import com.wenhua.wenhua.adpters.recyclerview.CommonAdapter;
import com.wenhua.wenhua.adpters.recyclerview.OnItemClickListener;
import com.wenhua.wenhua.adpters.recyclerview.ViewHolder;
import com.wenhua.wenhua.constants.Constants;
import com.wenhua.wenhua.controllers.base.BaseFragment;
import com.wenhua.wenhua.http.DefaultStringCallBack;
import com.wenhua.wenhua.models.beans.TreeViewBean;
import com.wenhua.wenhua.utils.ACache;
import com.wenhua.wenhua.utils.JsonUtils;
import com.wenhua.wenhua.utils.ListUtils;
import com.wenhua.wenhua.utils.ToastUtils;
import com.wenhua.wenhua.utils.photoviewer.PhotoView;
import com.wenhua.wenhua.utils.photoviewer.PhotoViewer;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by castiel on 2016/8/4.
 * 日志查询
 */
public class LogCheckFragment extends BaseFragment implements View.OnClickListener{

    private TextView mTvRegion;
    private TextView mTvRoute;
    private TextView mTvTower;
    private TextView mTvBeginCalendar;
    private TextView mTvBeginTime;
    private TextView mTvEndCalendar;
    private TextView mTvEndTime;
    private Button mBtnCheck;
    private RecyclerView mRecyclerView;
    private PhotoViewer mPhotoViewer;

    private Calendar mBeginDateCalendar;
    private Calendar mBeginTimeeCalendar;
    private Calendar mEndDateCalendar;
    private Calendar mEndTimeCalendar;
    private SimpleDateFormat mDateFormat;
    private SimpleDateFormat mTimeFormat;

    private List<String> imgs;
    private List<String> imgsLog;
    private CommonAdapter<String> mCommonAdapter;
    private String[] mRegions;
    private String[] mRoutes;
    private String[] mTowers;
    private int selectRegionsPosition = 0;
    private int selectRoutesPosition = 0;
    private int selectTowersPosition = 0;
    private List<TreeViewBean> mTreeData;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_logcheck);

        initData();
        initView();
        initListener();
    }

    @Override
    public void initView() {
        mTvRegion = (TextView) findViewById(R.id.logcheck_tv_region);
        mTvRoute = (TextView) findViewById(R.id.logcheck_tv_route);
        mTvTower = (TextView) findViewById(R.id.logcheck_tv_tower);
        mTvBeginCalendar = (TextView) findViewById(R.id.logcheck_tv_begin_calendar);
        mTvBeginTime = (TextView) findViewById(R.id.logcheck_tv_begin_time);
        mTvEndCalendar = (TextView) findViewById(R.id.logcheck_tv_end_calendar);
        mTvEndTime = (TextView) findViewById(R.id.logcheck_tv_end_time);
        mBtnCheck = (Button) findViewById(R.id.logcheck_btn_check);
        mRecyclerView = (RecyclerView) findViewById(R.id.logcheck_recyclerView);
        mPhotoViewer = (PhotoViewer) findViewById(R.id.logcheck_pohotoviewer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void initListener() {
        mTvBeginCalendar.setOnClickListener(this);
        mTvBeginTime.setOnClickListener(this);
        mTvEndCalendar.setOnClickListener(this);
        mTvEndTime.setOnClickListener(this);
        mTvRegion.setOnClickListener(this);
        mTvRoute.setOnClickListener(this);
        mTvTower.setOnClickListener(this);
        mBtnCheck.setOnClickListener(this);
    }

    private void initData(){
        mBeginDateCalendar = Calendar.getInstance();
        mBeginTimeeCalendar = Calendar.getInstance();
        mEndDateCalendar = Calendar.getInstance();
        mEndTimeCalendar = Calendar.getInstance();
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTimeFormat = new SimpleDateFormat("HH:mm:ss");

        String cameraList = ACache.get(getContext()).getAsString(Constants.ACACHE_CAMLISTJSON);
        mTreeData = JsonUtils.getInstance().json2List(cameraList, new TypeToken<List<TreeViewBean>>(){}.getType());
        mRegions = initArray(mTreeData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logcheck_tv_begin_calendar:
                DatePickerDialog datePickerDialog_begin = new DatePickerDialog(
                        getActivity(), new onDateSetListener(mBeginDateCalendar,0), mBeginDateCalendar
                        .get(Calendar.YEAR), mBeginDateCalendar
                        .get(Calendar.MONTH), mBeginDateCalendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog_begin.show();
                break;
            case R.id.logcheck_tv_begin_time:
                TimePickerDialog timePickerDialog_begin = new TimePickerDialog(
                        getActivity(),new onTimeSetListener(mBeginTimeeCalendar,0),mBeginTimeeCalendar
                        .get(Calendar.HOUR_OF_DAY),mBeginTimeeCalendar
                        .get(Calendar.MINUTE),false);
                timePickerDialog_begin.show();
                break;
            case R.id.logcheck_tv_end_calendar:
                DatePickerDialog datePickerDialog_end = new DatePickerDialog(
                        getActivity(), new onDateSetListener(mEndDateCalendar,1), mEndDateCalendar
                        .get(Calendar.YEAR), mEndDateCalendar
                        .get(Calendar.MONTH), mEndDateCalendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog_end.show();
                break;
            case R.id.logcheck_tv_end_time:
                TimePickerDialog timePickerDialog_end = new TimePickerDialog(
                        getActivity(),new onTimeSetListener(mEndTimeCalendar,1),mEndTimeCalendar
                        .get(Calendar.HOUR_OF_DAY),mEndTimeCalendar
                        .get(Calendar.MINUTE),false);
                timePickerDialog_end.show();
                break;
            case R.id.logcheck_tv_region:
                AlertDialog.Builder builder_region = new AlertDialog.Builder(getContext())
                        .setSingleChoiceItems(mRegions,selectRegionsPosition,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTvRegion.setText(mRegions[which]);
                                //父菜单发生变化
                                if(selectRegionsPosition != which){
                                    selectRegionsPosition = which;
                                    mTvRoute.setText("");
                                    mTvTower.setText("");
                                }
                                //子菜单数据更新
                                mRoutes = initArray(mTreeData.get(selectRegionsPosition).getChildren());

                                dialog.dismiss();
                            }
                        });
                builder_region.show();
                break;
            case R.id.logcheck_tv_route:
                AlertDialog.Builder builder_route = new AlertDialog.Builder(getContext())
                        .setSingleChoiceItems(mRoutes,selectRoutesPosition,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTvRoute.setText(mRoutes[which]);
                                if(selectRoutesPosition != which){
                                    selectRoutesPosition = which;
                                    mTvTower.setText("");
                                }
                                mTowers = initArray(mTreeData.get(selectRegionsPosition).getChildren().get(selectRoutesPosition).getChildren());

                                dialog.dismiss();
                            }
                        });
                builder_route.show();
                break;
            case R.id.logcheck_tv_tower:
                AlertDialog.Builder builder_tower = new AlertDialog.Builder(getContext())
                        .setSingleChoiceItems(mTowers,selectTowersPosition,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectTowersPosition = which;
                                mTvTower.setText(mTowers[which]);

                                dialog.dismiss();
                            }
                        });
                builder_tower.show();
                break;
            case R.id.logcheck_btn_check:
                if(!checkParamsIsEmpty()){
                    ToastUtils.show(getContext(),"查询条件不能为空");
                    return;
                }
                final ProgressDialog progressDialog = ProgressDialog.show(getContext(),"","正在查询结果",false);

                String params = "commandId=100&";
                params += "compname=" + mTvRegion.getText().toString() + "&";
                params += "sitearea=" + mTvRoute.getText().toString() + "&";
                params += "sitename=" + mTvTower.getText().toString() + "&";
                params += "startdate=" + mTvBeginCalendar.getText().toString() + "&";
                params += "starttime=" + mTvBeginTime.getText().toString() + "&";
                params += "enddate=" + mTvEndCalendar.getText().toString() + "&";
                params += "endtime=" + mTvEndTime.getText().toString();


                OkHttpUtils.postString()
                        //.url("http://"+ACache.get(getContext()).getAsString(Constants.ACACHE_IP)+"/wechatv2/CamLogSrch") //http://120.76.203.17/wechatv2/CamListJson)
                        .url("http://"+ACache.get(getContext()).getAsString(Constants.ACACHE_IP)+"/CamLogSrch") //http://120.76.203.17/wechatv2/CamListJson)
                        .content(params)
                        .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                        .tag(LogCheckFragment.this)
                        .build()
                        .execute(new DefaultStringCallBack(getContext()){

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                super.onError(call, e, id);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                super.onResponse(response, id);
                                progressDialog.dismiss();

                                int nRandomId = Integer.valueOf((String)JsonUtils.getInstance().getJsonValue(response,"nRandomId"));
                                int nRecordNumber = Integer.valueOf((String) JsonUtils.getInstance().getJsonValue(response,"nRecordNumber"));

                                imgs = new ArrayList<>();
                                imgsLog = new ArrayList<>();
                                for (int i = 0;i < nRecordNumber; i++){
                                  //  imgs.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1717816186,2947076118&fm=21&gp=0.jpg");
                                   // imgs.add("http://"+ACache.get(getContext()).getAsString(Constants.ACACHE_IP)+"/wechatv2/ImageLogIconFeedback?" +"RandomId=" + nRandomId + "&Number=" + i+"&gp=0.jpg");
                                    //  imgsLog.add("http://"+ACache.get(getContext()).getAsString(Constants.ACACHE_IP)+"/wechatv2/ImageLogFeedback?" + "RandomId=" + nRandomId + "&Number=" + i+"&gp=0.jpg");
                                    //The following is added by Bill Chen 20170717
                                    /*
                                     imgs.add("http://"+ACache.get(getContext()).getAsString(Constants.ACACHE_IP)+"/wechatv2/ImageLogIconFeedback?" +"RandomId=" + nRandomId + "&Number=" + i+"&gp="+i+".jpg");
                                     imgsLog.add("http://"+ACache.get(getContext()).getAsString(Constants.ACACHE_IP)+"/wechatv2/ImageLogFeedback?" + "RandomId=" + nRandomId + "&Number=" + i+"&gp="+i+".jpg");
                                     */
                                    imgs.add("http://"+ACache.get(getContext()).getAsString(Constants.ACACHE_IP)+"/ImageLogIconFeedback?" +"RandomId=" + nRandomId + "&Number=" + i+"&gp="+i+".jpg");
                                    imgsLog.add("http://"+ACache.get(getContext()).getAsString(Constants.ACACHE_IP)+"/ImageLogFeedback?" + "RandomId=" + nRandomId + "&Number=" + i+"&gp="+i+".jpg");
                                }

                                mCommonAdapter = new CommonAdapter<String>(getActivity(),R.layout.fragment_logcheck_item,imgs) {
                                    @Override
                                    public void convert(ViewHolder holder, String s) {
                                        Glide.with(getActivity())
                                                .load(s)
                                                .crossFade()
                                                .centerCrop()
                                                .placeholder(R.drawable.placeholder)
                                                .crossFade()
                                                .into((ImageView) holder.getView(R.id.logcheck_item_iv));
                                    }
                                };
                                mRecyclerView.setAdapter(mCommonAdapter);
                                mCommonAdapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                                        //mPhotoViewer.animateFrom((PhotoView) view.findViewById(R.id.logcheck_item_iv),imgs.get(position));
                                        mPhotoViewer.animateFrom((PhotoView) view.findViewById(R.id.logcheck_item_iv),imgsLog.get(position));
                                    }

                                    @Override
                                    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                                        return false;
                                    }
                                });
                            }
                        });
                break;
        }
    }

    /**
     * 给级联变量赋值
     * @param mList
     * @return
     */
    private String[] initArray(List<TreeViewBean> mList){
        if(!ListUtils.isEmpty(mList)){
            String[] str = new String[mList.size()];
            for (int i = 0; i < mList.size() ;i++){
                str[i] = mList.get(i).getText();
            }
            return str;
        }
        return null;
    }

    /**
     * 检查查询条件是否为空
     * @return
     */
    private boolean checkParamsIsEmpty(){
        boolean flag = true;
        if(TextUtils.isEmpty(mTvRegion.getText())){
            flag = false;
        }
        if(TextUtils.isEmpty(mTvRoute.getText())){
            flag = false;
        }
        if(TextUtils.isEmpty(mTvTower.getText())){
            flag = false;
        }
        if(TextUtils.isEmpty(mTvBeginCalendar.getText())){
            flag = false;
        }
        if(TextUtils.isEmpty(mTvBeginTime.getText())){
            flag = false;
        }
        if(TextUtils.isEmpty(mTvEndCalendar.getText())){
            flag = false;
        }
        if(TextUtils.isEmpty(mTvEndTime.getText())){
            flag = false;
        }
        return flag;
    }

    /**
     * 日期选择器监听内部类
     */
    private class onDateSetListener implements DatePickerDialog.OnDateSetListener {

        private Calendar calendar;
        private int type; //0 begin  1 end

        onDateSetListener(Calendar calendar,int type){
            this.calendar = calendar;
            this.type = type;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // 每次保存设置的日期
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if(type == 0){
                mTvBeginCalendar.setText(mDateFormat.format(calendar.getTime()));
            }else{
                mTvEndCalendar.setText(mDateFormat.format(calendar.getTime()));
            }
        }
    }

    /**
     * 时间选择器监听内部类
     */
    private class onTimeSetListener implements TimePickerDialog.OnTimeSetListener{
        private Calendar calendar;
        private int type; //0 begin  1 end

        onTimeSetListener(Calendar calendar,int type){
            this.calendar = calendar;
            this.type = type;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            calendar.set(Calendar.SECOND,0);

            if(type == 0){
                mTvBeginTime.setText(mTimeFormat.format(calendar.getTime()));
            }else{
                mTvEndTime.setText(mTimeFormat.format(calendar.getTime()));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(LogCheckFragment.this);
    }
}
