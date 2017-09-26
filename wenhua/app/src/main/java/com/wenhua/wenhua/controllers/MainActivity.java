package com.wenhua.wenhua.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.shizhefei.view.viewpager.SViewPager;
import com.wenhua.wenhua.R;
import com.wenhua.wenhua.controllers.base.BaseActivity;
import com.wenhua.wenhua.utils.ToastUtils;

/**
 * Created by castiel on 2016/8/4.
 */
public class MainActivity extends BaseActivity {

    private SViewPager mSViewPager;
    private FixedIndicatorView mFixedIndicatorView;
    private IndicatorViewPager mIndicatorViewPager;
    //The following is added by Bill Chen
    public static String sToken="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //The following is added by Bill Chen 20170717
        /*
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        */
        //The above is added by Bill Chen 20170717
        setContentView(R.layout.activity_main);

        //The following is added by Bill Chen 20161008
        Intent intent=getIntent();
        sToken=intent.getStringExtra("token");
        //The above is added by  Bill Chen

        initView();
        initListener();
    }


    @Override
    public void initView() {
        mSViewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
        mFixedIndicatorView = (FixedIndicatorView) findViewById(R.id.tabmain_indicator);
        mFixedIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(Color.RED, Color.GRAY));
        mIndicatorViewPager = new IndicatorViewPager(mFixedIndicatorView, mSViewPager);
        mIndicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        // 禁止viewpager的滑动事件
        // mSViewPager.setCanScroll(false);
        // 设置viewpager保留界面不重新加载的页面数量
        mSViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void initListener() {

    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] tabNames = {"摄像机管理", "日志查询", "GIS显示"};
        private int[] tabIcons = {R.drawable.maintab_1_selector, R.drawable.maintab_2_selector, R.drawable.maintab_3_selector};
        private LayoutInflater inflater;

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            inflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tab_main, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabNames[position]);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[position], 0, 0);
            return textView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            switch (position){
                case 0: {
                    Fragment aa=new CameraFragment();
                    return aa;
                    //return new CameraFragment();
                }
                case 1:
                    return new LogCheckFragment();
                case 2:
                    return new GisFragment();
            }
            return null;
        }
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - exitTime > 2000){
            ToastUtils.show(MainActivity.this,"再按一次退出程序");
            exitTime = System.currentTimeMillis();
        }else{
            finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
