package com.wind.control.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wind.control.R;
import com.wind.control.adapter.SceneDeviceAdapter;
import com.wind.control.base.BaseActivity;
import com.wind.control.model.DeviceListBean;
import com.wind.control.model.SceneDeviceBean;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.okhttp.service.QueryDeviceRequest;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.Constants;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.NetUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by 1003373 on 2018/8/22.
 */

public class SceneAddActionActivity extends BaseActivity {
    @BindView(R.id.tv_activity_title)
    TextView mTvActivityTitle;
    private RecyclerView mRecyclerView;
    private static final String TAG = "SceneAddActionActivity";
    private ArrayList<DeviceListBean.DevicesBean> mDataList = new ArrayList<>();
    private SceneDeviceAdapter mSceneDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);
        ButterKnife.bind(this);

        setActivityTitle(mTvActivityTitle, R.string.add_action);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestQueryDevice();
    }

    private void requestQueryDevice() {
        QueryDeviceRequest request = new QueryDeviceRequest();
        request.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        request.setToken(BaseInfoSPUtil.getInstance().getLoginToken(this));

        OkHttpUtils.postString()
                .url(Api.QUERY_DEVICE_LIST)
                .content(new Gson().toJson(request))
                .build()
                .execute(new GenericsCallback<DeviceListBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(SceneAddActionActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(DeviceListBean response, int id, int code) {
                        mDataList.clear();
                        String mCode = response.getCode();
                        if (mCode.equals("1000")) {
                            mDataList.addAll(response.getDevices());
                            mSceneDeviceAdapter.notifyDataSetChanged();
                        } else {
                            showToast(response.getMsg());
                        }
                    }
                });
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_device_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mSceneDeviceAdapter = new SceneDeviceAdapter(mDataList);
        mRecyclerView.setAdapter(mSceneDeviceAdapter);
        mSceneDeviceAdapter.setOnItemClickListner(new SceneDeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mDataList.get(position).getType().equals("1")) {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("id", mDataList.get(position).getId());
                    mIntent.putExtra("name", mDataList.get(position).getName());
                    mIntent.putExtra("type", mDataList.get(position).getType());
                    mIntent.setClass(SceneAddActionActivity.this, SceneLightControlActivity.class);
                    startActivityForResult(mIntent, 0);
                } else if (mDataList.get(position).getType().equals("2")) {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("id", mDataList.get(position).getId());
                    mIntent.putExtra("name", mDataList.get(position).getName());
                    mIntent.putExtra("type", mDataList.get(position).getType());
                    mIntent.setClass(SceneAddActionActivity.this, SceneAircControlActivity.class);
                    startActivityForResult(mIntent, 0);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == Constants.ARICCODE) {
                setResult(Constants.ARICCODE, intent);
                SceneAddActionActivity.this.finish();
            } else if (resultCode == Constants.LIGHTCODE) {
                setResult(Constants.LIGHTCODE, intent);
                SceneAddActionActivity.this.finish();
            }
        }
    }
}
