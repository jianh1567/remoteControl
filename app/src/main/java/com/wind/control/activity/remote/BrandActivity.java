package com.wind.control.activity.remote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.wind.control.MyApplication;
import com.wind.control.R;
import com.wind.control.adapter.BrandAdapter;
import com.wind.control.base.BaseActivity;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.NoLoginUtils;

import net.irext.webapi.WebAPICallbacks;
import net.irext.webapi.model.Brand;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：Created by luow on 2018/8/7
 * 注释：
 */
public class BrandActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = "BrandActivity";
    @BindView(R.id.lv_brand_list)
    ListView mLvBrandList;
    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    private MyApplication mApp;
    private List<Brand> mBrands;
    private BrandAdapter mBrandAdapter;
    private int mId;
    private int mPrePageCount = 40;
    private int mCurrentPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        ButterKnife.bind(this);
        mApp = (MyApplication) getApplication();
        mId = getIntent().getIntExtra("id", 0);
        Log.d("lw", mId + "");
        mBrands = new ArrayList<>();
        mBrandAdapter = new BrandAdapter(BrandActivity.this, mBrands);
        mLvBrandList.setOnItemClickListener(this);
        mLvBrandList.setAdapter(mBrandAdapter);
        listBrands(mCurrentPage);
    }

    private WebAPICallbacks.ListBrandsCallback mListBrandsCallback = new WebAPICallbacks.ListBrandsCallback() {
        @Override
        public void onListBrandsSuccess(List<Brand> brands) {
            mBrands.addAll(brands);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mBrands.size() == 0) {
                        Toast.makeText(BrandActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                    mBrandAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onListBrandsFailed() {
            Log.w(TAG, "list brands failed");
        }

        @Override
        public void onListBrandsError() {
            Log.e(TAG, "list brands error");
        }
    };

    private void listBrands(final int currentPage) {
        new Thread() {
            @Override
            public void run() {
                mApp.mWeAPIs
                        .listBrands(mId, currentPage, mPrePageCount,
                                mListBrandsCallback);
            }
        }.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (TextUtils.isEmpty(BaseInfoSPUtil.getInstance().getLoginToken(this))){
            NoLoginUtils.isLogin(this);
            return;
        }

        if (mId == 1) {
            //空调
            Intent itStb = new Intent(this, SelectDeviceActivity.class);
            itStb.putExtra("brandId", mBrands.get(position).getId());
            itStb.putExtra("id", mId);
            itStb.putExtra("name", mBrands.get(position).getName());
            startActivity(itStb);
        } else if (mId == 7) {
            //风扇
//            Intent itStb = new Intent(this, FanActivity.class);
//            itStb.putExtra("brandId", mBrands.get(position).getId());
//            itStb.putExtra("id", mId);
//            itStb.putExtra("name", mBrands.get(position).getName());
//            startActivity(itStb);
        }
    }

    @OnClick({R.id.iv_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
        }
    }
}
