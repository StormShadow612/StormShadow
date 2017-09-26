package com.wenhua.wenhua.controllers;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.wenhua.wenhua.R;
import com.wenhua.wenhua.adpters.IconTreeItemHolder;
import com.wenhua.wenhua.constants.Constants;
import com.wenhua.wenhua.controllers.base.BaseFragment;
import com.wenhua.wenhua.http.DefaultStringCallBack;
import com.wenhua.wenhua.models.beans.TreeViewBean;
import com.wenhua.wenhua.utils.ACache;
import com.wenhua.wenhua.utils.JsonUtils;
import com.wenhua.wenhua.utils.ListUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.List;

import okhttp3.Call;

/**
 * Created by castiel on 2016/8/4.
 * 摄像机管理
 */

public class CameraFragment extends BaseFragment {

    private ViewGroup mContainerView;
    private TextView mTvError;
    private AndroidTreeView mAndroidTreeView;
    private List<TreeViewBean> mData;
    private RelativeLayout mRelayoutErrorToast;
    private Button mBtnRetry;
    private ProgressBar mProgressBar;



    //The following is added by Bill Chen
    public String getCameraTips(String camId)
    {
        String result=null;
        return result;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_camera);

        initView();
        initListener();
        loadNetData();

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                mAndroidTreeView.restoreState(state);
            }
        }
    }

    @Override
    public void initView() {
        mContainerView = (ViewGroup) findViewById(R.id.container);
        mTvError = (TextView) findViewById(R.id.cameralist_tv_error);
        mRelayoutErrorToast = (RelativeLayout) findViewById(R.id.cameralist_relayout_error_toast);
        mBtnRetry = (Button) findViewById(R.id.cameralist_btn_retry);
        mProgressBar = (ProgressBar) findViewById(R.id.cameralist_progressBar);
    }

    private void loadNetData(){
        mRelayoutErrorToast.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        OkHttpUtils.get()
                //.url("http://"+ACache.get(getContext()).getAsString(Constants.ACACHE_IP)+"/wechatv2/CamListJson?sToken="+MainActivity.sToken) //http://120.76.203.17/wechatv2/CamListJson
                .url("http://"+ACache.get(getContext()).getAsString(Constants.ACACHE_IP)+"/CamListJson?sToken="+MainActivity.sToken) //http://120.76.203.17/wechatv2/CamListJson
                .tag(CameraFragment.this)
                .build()
                .execute(new DefaultStringCallBack(getContext()){

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mProgressBar.setVisibility(View.GONE);
                        mRelayoutErrorToast.setVisibility(View.VISIBLE);
                        mTvError.setText("网络异常");
                        mBtnRetry.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mProgressBar.setVisibility(View.GONE);
                        if(TextUtils.isEmpty(response)){
                            mRelayoutErrorToast.setVisibility(View.VISIBLE);
                            mBtnRetry.setVisibility(View.INVISIBLE);
                        }else{
                            mRelayoutErrorToast.setVisibility(View.GONE);
                            ACache.get(getContext()).put(Constants.ACACHE_CAMLISTJSON,response);
                            mData = JsonUtils.getInstance().json2List(response, new TypeToken<List<TreeViewBean>>(){}.getType());
                            initTree();
                        }
                    }
                });
    }

    @Override
    public void initListener() {
        mBtnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNetData();
            }
        });
    }

    private void initTree(){
        if(!ListUtils.isEmpty(mData)){
            TreeNode root = TreeNode.root();
            for (int i = 0; i < mData.size(); i++) {
                TreeViewBean treeViewBean1 = mData.get(i);
                TreeNode treeNode1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(treeViewBean1)).setViewHolder(new IconTreeItemHolder(getActivity()));
                if(!ListUtils.isEmpty(treeViewBean1.getChildren())){
                    for(int j = 0; j < treeViewBean1.getChildren().size();j++){
                        TreeViewBean treeViewBean2 = treeViewBean1.getChildren().get(j);
                        TreeNode treeNode2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(treeViewBean2)).setViewHolder(new IconTreeItemHolder(getActivity()));;

                        if(!ListUtils.isEmpty(treeViewBean2.getChildren())){
                            for(int k = 0; k < treeViewBean2.getChildren().size();k++){
                                TreeViewBean treeViewBean3 = treeViewBean2.getChildren().get(k);
                                TreeNode treeNode3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(treeViewBean3)).setViewHolder(new IconTreeItemHolder(getActivity()));;

                                treeNode2.addChild(treeNode3);
                            }
                        }
                        treeNode1.addChild(treeNode2);
                    }
                }
                root.addChild(treeNode1);
            }

            mAndroidTreeView = new AndroidTreeView(getActivity(), root);
            mAndroidTreeView.setDefaultAnimation(true);
           // mAndroidTreeView.setDefaultNodeClickListener(nodeClickListener);
            mAndroidTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom, true);
            mContainerView.addView(mAndroidTreeView.getView());
            //The following is added by Bill Chen 20170716
            mAndroidTreeView.expandAll();
            //The above is added
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", mAndroidTreeView.getSaveState());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(CameraFragment.this);
    }
}
