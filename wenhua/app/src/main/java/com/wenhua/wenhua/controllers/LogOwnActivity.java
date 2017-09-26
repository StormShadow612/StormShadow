package com.wenhua.wenhua.controllers;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class LogOwnActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
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
    private List<TreeViewBean> mTreeData;

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_details);
        Intent intent = getIntent();
        String region = intent.getStringExtra("region");
        String strLine = intent.getStringExtra("line");
        String tower = intent.getStringExtra("tower");

        findViewById(R.id.Ll2).setVisibility(View.INVISIBLE);
        findViewById(R.id.Ll3).setVisibility(View.INVISIBLE);
        mTvRegion = (TextView) findViewById(R.id.logcheck_tv_region1);

        mTvRoute = (TextView) findViewById(R.id.logcheck_tv_route);
        mTvTower = (TextView) findViewById(R.id.logcheck_tv_tower);
        mTvBeginCalendar = (TextView) findViewById(R.id.logcheck_tv_begin_calendar);
        mTvBeginTime = (TextView) findViewById(R.id.logcheck_tv_begin_time);
        mTvEndCalendar = (TextView) findViewById(R.id.logcheck_tv_end_calendar);
        mTvEndTime = (TextView) findViewById(R.id.logcheck_tv_end_time);
        mBtnCheck = (Button) findViewById(R.id.logcheck_btn_check);
        mRecyclerView = (RecyclerView) findViewById(R.id.logcheck_recyclerView);
        mPhotoViewer = (PhotoViewer) findViewById(R.id.logcheck_pohotoviewer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mTvRegion.setText(region);
        mTvRoute.setText(strLine);
        mTvTower.setText(tower);

        mBeginDateCalendar = Calendar.getInstance();
        mBeginTimeeCalendar = Calendar.getInstance();
        mEndDateCalendar = Calendar.getInstance();
        mEndTimeCalendar = Calendar.getInstance();
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTimeFormat = new SimpleDateFormat("HH:mm:ss");

        String cameraList = ACache.get(LogOwnActivity.this).getAsString(Constants.ACACHE_CAMLISTJSON);
        mTreeData = JsonUtils.getInstance().json2List(cameraList, new TypeToken<List<TreeViewBean>>() {
        }.getType());
        String[] mRegions = initArray(mTreeData);

        mTvBeginCalendar.setOnClickListener(this);
        mTvBeginTime.setOnClickListener(this);
        mTvEndCalendar.setOnClickListener(this);
        mTvEndTime.setOnClickListener(this);
       /* mTvRegion.setOnClickListener(this);
        mTvRoute.setOnClickListener(this);
        mTvTower.setOnClickListener(this);*/
        mBtnCheck.setOnClickListener(this);

        mTvRegion.setText("区域："+region+" 线路:"+strLine +" 塔号:"+tower);
        //String strToshow="区域："+region+" 线路:"+strLine +" 塔号:"+tower;
        String strToshow="文华GPRS";
        toolbar= (Toolbar) findViewById(R.id.toolbarLog);
        toolbar.setTitle(strToshow);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.baturn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logcheck_tv_begin_calendar:
                DatePickerDialog datePickerDialog_begin = new DatePickerDialog(
                        LogOwnActivity.this, new onDateSetListener(mBeginDateCalendar, 0), mBeginDateCalendar
                        .get(Calendar.YEAR), mBeginDateCalendar
                        .get(Calendar.MONTH), mBeginDateCalendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog_begin.show();
                break;
            case R.id.logcheck_tv_begin_time:
                TimePickerDialog timePickerDialog_begin = new TimePickerDialog(
                        LogOwnActivity.this, new onTimeSetListener(mBeginTimeeCalendar, 0), mBeginTimeeCalendar
                        .get(Calendar.HOUR_OF_DAY), mBeginTimeeCalendar
                        .get(Calendar.MINUTE), false);
                timePickerDialog_begin.show();
                break;
            case R.id.logcheck_tv_end_calendar:
                DatePickerDialog datePickerDialog_end = new DatePickerDialog(
                        LogOwnActivity.this, new onDateSetListener(mEndDateCalendar, 1), mEndDateCalendar
                        .get(Calendar.YEAR), mEndDateCalendar
                        .get(Calendar.MONTH), mEndDateCalendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog_end.show();
                break;
            case R.id.logcheck_tv_end_time:
                TimePickerDialog timePickerDialog_end = new TimePickerDialog(
                        LogOwnActivity.this, new LogOwnActivity.onTimeSetListener(mEndTimeCalendar, 1), mEndTimeCalendar
                        .get(Calendar.HOUR_OF_DAY), mEndTimeCalendar
                        .get(Calendar.MINUTE), false);
                timePickerDialog_end.show();
                break;
            case R.id.logcheck_btn_check:
                if (!checkParamsIsEmpty()) {
                    ToastUtils.show(LogOwnActivity.this, "查询条件不能为空");
                    return;
                }
                final ProgressDialog progressDialog = ProgressDialog.show(LogOwnActivity.this, "", "正在查询结果", false);

                String params = "commandId=100&";
                params += "compname=" + mTvRegion.getText().toString() + "&";
                params += "sitearea=" + mTvRoute.getText().toString() + "&";
                params += "sitename=" + mTvTower.getText().toString() + "&";
                params += "startdate=" + mTvBeginCalendar.getText().toString() + "&";
                params += "starttime=" + mTvBeginTime.getText().toString() + "&";
                params += "enddate=" + mTvEndCalendar.getText().toString() + "&";
                params += "endtime=" + mTvEndTime.getText().toString();


                OkHttpUtils.postString()
                        .url("http://" + ACache.get(LogOwnActivity.this).getAsString(Constants.ACACHE_IP) + "/CamLogSrch") //http://120.76.203.17/wechatv2/CamListJson)
                        .content(params)
                        .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                        .tag(LogOwnActivity.this)
                        .build()
                        .execute(new DefaultStringCallBack(LogOwnActivity.this) {

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                super.onError(call, e, id);
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                super.onResponse(response, id);
                                progressDialog.dismiss();

                                int nRandomId = Integer.valueOf((String) JsonUtils.getInstance().getJsonValue(response, "nRandomId"));
                                int nRecordNumber = Integer.valueOf((String) JsonUtils.getInstance().getJsonValue(response, "nRecordNumber"));

                                imgs = new ArrayList<>();
                                imgsLog = new ArrayList<>();
                                for (int i = 0; i < nRecordNumber; i++) {
                                    imgs.add("http://" + ACache.get(LogOwnActivity.this).getAsString(Constants.ACACHE_IP) + "/ImageLogIconFeedback?" + "RandomId=" + nRandomId + "&Number=" + i + "&gp=" + i + ".jpg");
                                    imgsLog.add("http://" + ACache.get(LogOwnActivity.this).getAsString(Constants.ACACHE_IP) + "/ImageLogFeedback?" + "RandomId=" + nRandomId + "&Number=" + i + "&gp=" + i + ".jpg");
                                }

                                mCommonAdapter = new CommonAdapter<String>(LogOwnActivity.this, R.layout.fragment_logcheck_item, imgs) {
                                    @Override
                                    public void convert(ViewHolder holder, String s) {
                                        Glide.with(LogOwnActivity.this)
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
                                        mPhotoViewer.animateFrom((PhotoView) view.findViewById(R.id.logcheck_item_iv), imgsLog.get(position));
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
     *
     * @param mList
     * @return
     */
    private String[] initArray(List<TreeViewBean> mList) {
        if (!ListUtils.isEmpty(mList)) {
            String[] str = new String[mList.size()];
            for (int i = 0; i < mList.size(); i++) {
                str[i] = mList.get(i).getText();
            }
            return str;
        }
        return null;
    }

    /**
     * 检查查询条件是否为空
     *
     * @return
     */
    private boolean checkParamsIsEmpty() {
        boolean flag = true;
        if (TextUtils.isEmpty(mTvBeginCalendar.getText())) {
            flag = false;
        }
        if (TextUtils.isEmpty(mTvBeginTime.getText())) {
            flag = false;
        }
        if (TextUtils.isEmpty(mTvEndCalendar.getText())) {
            flag = false;
        }
        if (TextUtils.isEmpty(mTvEndTime.getText())) {
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

        onDateSetListener(Calendar calendar, int type) {
            this.calendar = calendar;
            this.type = type;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // 每次保存设置的日期
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (type == 0) {
                mTvBeginCalendar.setText(mDateFormat.format(calendar.getTime()));
            } else {
                mTvEndCalendar.setText(mDateFormat.format(calendar.getTime()));
            }
        }
    }

    /**
     * 时间选择器监听内部类
     */
    private class onTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        private Calendar calendar;
        private int type; //0 begin  1 end

        onTimeSetListener(Calendar calendar, int type) {
            this.calendar = calendar;
            this.type = type;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if (type == 0) {
                mTvBeginTime.setText(mTimeFormat.format(calendar.getTime()));
            } else {
                mTvEndTime.setText(mTimeFormat.format(calendar.getTime()));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(LogOwnActivity.this);
    }
}

