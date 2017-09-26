package com.wenhua.wenhua.controllers;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.view.View;

import com.wenhua.wenhua.R;
import com.wenhua.wenhua.adpters.AllcameraAdpter;
import com.wenhua.wenhua.constants.Constants;
import com.wenhua.wenhua.models.imageLoader.ImageAndText;
import com.wenhua.wenhua.utils.ACache;

import java.util.ArrayList;
import java.util.List;

public class CameraAllDetailsActivity extends AppCompatActivity {
    public String strCameraId;
    public String strPhoneNumber;
    public String strCameraLine;
    private ListView listView;

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
        setContentView(R.layout.activity_all_camera_details);
        Intent intent = getIntent();
        final String strLine = intent.getStringExtra("line");
        final ArrayList tower = intent.getStringArrayListExtra("tower");
        final ArrayList tip = intent.getStringArrayListExtra("tips");
        List<ImageAndText> imageText = new ArrayList<ImageAndText>();
        for (int i = 0; i < tip.size(); i++) {
            String tips = (String) tip.get(i);
            String towers = (String) tower.get(i);
            String strAdrToken = "地址:";
            String strPhoneToken = "号码:";
            String strDevToken = "设备标识:";
            int nAdrIndex = tips.indexOf(strAdrToken);
            int nPhoneIndex = tips.indexOf(strPhoneToken);
            int nDevIndex = tips.indexOf(strDevToken);
            String strID = tips.substring(nDevIndex + strDevToken.length(), nPhoneIndex - 1);
            String strPhone = tips.substring(nPhoneIndex + strPhoneToken.length(), nAdrIndex - 1);
            strCameraId = strID.trim();
            strPhoneNumber = strPhone.trim();
            strCameraLine = strLine.trim();
            int value = (int) (Math.random() * 100);
            String dumId = value + ".jpg";
            String strRealLiveImage = "http://" + ACache.get(getApplicationContext()).getAsString(Constants.ACACHE_IP) + "/AppLiveImageShow?camId=" + strCameraId + "&dumid=" + dumId;
            ImageAndText imageAndText = new ImageAndText(strRealLiveImage, towers);
            imageText.add(imageAndText);
        }
        listView = (ListView) findViewById(R.id.imageList);
        AllcameraAdpter adpter = new AllcameraAdpter(this, imageText, listView);
        listView.setAdapter(adpter);
        adpter.setOnItemClickListener(new AllcameraAdpter.onItemListener() {
            @Override
            public void onItemClick(int position) {
                String tips = (String) tip.get(position);
                String towers = (String) tower.get(position);
                Intent intent = new Intent(CameraAllDetailsActivity.this, CameraDetailsActivity.class);
                intent.putExtra("tips", tips);
                intent.putExtra("line", strLine.trim());
                intent.putExtra("tower", towers);
                startActivity(intent);
            }
        });
        String strToshow = "所有像机-线路:" + strLine.trim();
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbarList);
        toolbar1.setTitle(strToshow);
        setSupportActionBar(toolbar1);
        toolbar1.setNavigationIcon(R.mipmap.baturn);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
