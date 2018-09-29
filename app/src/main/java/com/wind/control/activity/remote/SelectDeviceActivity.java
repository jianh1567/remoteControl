package com.wind.control.activity.remote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wind.control.R;
import com.wind.control.activity.device.BindDeviceActivity;
import com.wind.control.adapter.SelectDeviceAdapter;
import com.wind.control.base.BaseActivity;
import com.wind.control.model.HttpResponse;
import com.wind.control.model.SelectDeviceBean;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.okhttp.service.BindDeviceRequest;
import com.wind.control.okhttp.service.QueryDeviceRequest;
import com.wind.control.util.AppManager;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.ClickUtils;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.NetUtil;
import com.wind.control.view.IOSAlertDialog;
import com.wind.control.widget.ConfirmDialog;
import com.wind.control.widget.listview.XSwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/8/11
 * 注释：
 */
public class SelectDeviceActivity extends BaseActivity implements AdapterView.OnItemClickListener, XSwipeMenuListView.IXListViewListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private int mId;
    private int mBrandId;
    private String mName;
    @BindView(R.id.device_list)
    XSwipeMenuListView mDeviceList;
    private List<SelectDeviceBean.TriadinfoBean> mDataList;
    private SelectDeviceAdapter mAdapter;
    private static final String TAG = "SelectDeviceActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);
        mId = getIntent().getIntExtra("id", 0);
        mBrandId = getIntent().getIntExtra("brandId", 0);
        mName = getIntent().getStringExtra("name");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestQueryDevice();
    }

    private void initView() {
        mDeviceList.setOnItemClickListener(this);
        mDeviceList.setPullRefreshEnable(true);
        mDeviceList.setPullLoadEnable(false);
        //刷新和加载更多
        mDeviceList.setXListViewListener(this);
        mDataList = new ArrayList<>();
        mAdapter = new SelectDeviceAdapter(this, mDataList);
        mDeviceList.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
//            case R.id.ll_device_one:
//                showConfirmDialog("IR_1");
//                break;
//            case R.id.ll_device_two:
//                showConfirmDialog("IR_2");
//                break;
//            case R.id.ll_device_three:
//                showConfirmDialog("IR_3");
//                break;
        }
    }

    private void requestQueryDevice() {
        Log.i(TAG, "requestQueryDevice");
        if (TextUtils.isEmpty(BaseInfoSPUtil.getInstance().getLoginToken(this))) {
            mDeviceList.setVisibility(View.GONE);
            return;
        }
        mDeviceList.setVisibility(View.VISIBLE);
        QueryDeviceRequest request = new QueryDeviceRequest();
        request.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        request.setToken(BaseInfoSPUtil.getInstance().getLoginToken(this));
        OkHttpUtils.postString()
                .url(Api.QUERY_TRIAD)
                .content(new Gson().toJson(request))
                .build()
                .execute(new GenericsCallback<SelectDeviceBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        Log.i(TAG, "requestQueryDevice onError code = " + code);
                        if (!NetUtil.isNetworkAvailable(SelectDeviceActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(SelectDeviceBean response, int id, int code) {
                        mDataList.clear();
                        int mCode = response.getCode();
                        Log.i(TAG, "requestQueryDevice onResponse mCode = " + mCode);
                        if (mCode == 1000) {
                            if (response.getTriadinfo().size() == 0) {
                                mDeviceList.setVisibility(View.GONE);
                                IOSAlertDialog iosAlertDialog = new IOSAlertDialog(SelectDeviceActivity.this);
                                iosAlertDialog.builder().setTitle("温馨提示").setMessage("您当前还未添加过任何红外设备，是否前去添加?")
                                        .setNegativeButton("取消", new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {

                                            }

                                        }).setPositiveButton("去添加", new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        if (ClickUtils.isContinual()) {
                                            return;
                                        }
                                        openActivity(BindDeviceActivity.class);
                                    }
                                }).show();
                            }
                            mDeviceList.stopRefresh();
                            mDataList.addAll(response.getTriadinfo());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            //   showToast(response.getMsg());
                        }
                    }
                });
    }

    private void showConfirmDialog(final String deviceName) {
        final ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.builder().setCancelable(false).setTitle("给设备起个名字吧").
                setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(dialog.getMoney())) {
                            showToast("给设备起个名字吧");
                        } else {
                            dialog.hideInputMethod();
                            dialog.closeDialog();
                            requestBindDevice(deviceName, dialog.getMoney());
                        }
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hideInputMethod();
                dialog.closeDialog();
            }
        }).show();
    }

    /**
     * 绑定设备
     */
    private void requestBindDevice(String deviceName, String title) {
        BindDeviceRequest request = new BindDeviceRequest();
        request.setType("2");//1 蓝牙  2 遥控
        request.setMac("");
        request.setTitle(title);
        request.setName(mName);
        request.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        request.setToken(BaseInfoSPUtil.getInstance().getLoginToken(this));
        request.setBrandid(mBrandId);
        request.setCategoryid(mId);
        request.setIr_devicename(deviceName);
        OkHttpUtils.postString()
                .url(Api.BIND_DEVICE)
                .content(new Gson().toJson(request))
                .build()
                .execute(new GenericsCallback<HttpResponse>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(SelectDeviceActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(HttpResponse response, int id, int code) {

                        String mCode = response.getCode();
                        if (mCode.equals("1000")) {
                            showToast(response.getMsg());
                            AppManager.getInstance().finishActivity(SelecteTypeActivity.class);
                            AppManager.getInstance().finishActivity(BrandActivity.class);
                            finish();
                        } else {
                            showToast(response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showConfirmDialog(mDataList.get(position - 1).getDeviceName());
    }

    @Override
    public void onRefresh() {
        requestQueryDevice();
    }

    @Override
    public void onLoadMore() {

    }
}