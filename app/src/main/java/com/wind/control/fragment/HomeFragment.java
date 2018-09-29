package com.wind.control.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.activity.CaptureActivity;
import com.wind.control.R;
import com.wind.control.activity.ScanConnectActivity;
import com.wind.control.activity.remote.AcActivity;
import com.wind.control.activity.remote.LightControl1Activity;
import com.wind.control.activity.remote.SelecteTypeActivity;
import com.wind.control.activity.remote.SocketControlActivity;
import com.wind.control.adapter.HomeListAdapter;
import com.wind.control.base.BaseActivity;
import com.wind.control.base.BaseFragment;
import com.wind.control.interfaces.PermissionListener;
import com.wind.control.model.DeviceListBean;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.okhttp.service.QueryDeviceRequest;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.Constants;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.NetUtil;
import com.wind.control.view.IOSAlertDialog;
import com.wind.control.widget.SelectConnectDialog;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * 作者：Created by luow on 2018/7/9
 * 注释：
 */
public class HomeFragment extends BaseFragment implements HomeListAdapter.onItemClickListener {
    private static final int CODE = 11;
    private static final String TAG = "HomeFragment";
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.iv_add)
    ImageView mIvAdd;
    @BindView(R.id.rl_other)
    RelativeLayout mRlOther;
    @BindView(R.id.rl_add_device)
    RelativeLayout mRlAddDevice;
    @BindView(R.id.recyview)
    RecyclerView mRecyview;

    private View mPopView;
    private PopupWindow mPopupWindow;
    private boolean mPopIsShow = true;
    private LinearLayout mAddDevice;
    private LinearLayout mDeviceStyle;
    private LinearLayout mLlQrCode;
    private LinearLayout mWifiScan;
    private List<DeviceListBean.DevicesBean> mDataList;
    private HomeListAdapter mAdapter;
    private boolean isHidden;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }

        initView();
        initLitener();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isHidden = false;
            requestQueryDevice();
        } else {
            isHidden = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isHidden) {
            return;
        }
        requestQueryDevice();
    }

    private void requestQueryDevice() {
        if (TextUtils.isEmpty(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))) {
            mRlAddDevice.setVisibility(View.VISIBLE);
            mRecyview.setVisibility(View.GONE);
            mTvLogin.setText("未登录");
            return;
        }
        QueryDeviceRequest request = new QueryDeviceRequest();
        request.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(getActivity()));
        request.setToken(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()));
        OkHttpUtils.postString()
                .url(Api.QUERY_DEVICE_LIST)
                .content(new Gson().toJson(request))
                .build()
                .execute(new GenericsCallback<DeviceListBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(getActivity())) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(DeviceListBean response, int id, int code) {
                        String mCode = response.getCode();
                        mDataList.clear();
                        if (mCode.equals("1000")) {
                            mTvLogin.setText("已登录");

                            mDataList.addAll(response.getDevices());

                            if (mDataList.size() == 0) {
                                mRlAddDevice.setVisibility(View.VISIBLE);
                                mRecyview.setVisibility(View.GONE);
                            } else {
                                mRlAddDevice.setVisibility(View.GONE);
                                mRecyview.setVisibility(View.VISIBLE);
                                mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            showToast(response.getMsg());
                        }
                    }
                });
    }

    @OnClick({R.id.tv_login, R.id.iv_add, R.id.rl_add_device})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                break;
            case R.id.iv_add:
                if (!mPopIsShow) {
                    mPopIsShow = true;
                    mPopupWindow.dismiss();
                } else {

                    ObjectAnimator ra = ObjectAnimator.ofFloat(mIvAdd, "rotation", 0f, 50f);
                    ra.setDuration(300);
                    ra.start();
                    mPopIsShow = false;
                    mPopupWindow.showAsDropDown(mRlOther, SupportMultipleScreensUtil.getScaleValue(595), 0);
                }
                break;
            case R.id.rl_add_device:
                final SelectConnectDialog dialog = new SelectConnectDialog(getActivity());
                dialog.builder().setBlueConnect(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openActivity(ScanConnectActivity.class);
                        dialog.closeDialog();
                    }
                }).setScanConnect(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CaptureActivity.class);
                        startActivityForResult(intent, Constants.REQ_QR_CODE);
                        dialog.closeDialog();
                    }
                }).setWifiConnect(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openActivity(SelecteTypeActivity.class);
                        dialog.closeDialog();
                    }
                }).show();
                break;
        }
    }

    private void initView() {
        if (mPopupWindow == null) {
            mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_pop_view, null);
            SupportMultipleScreensUtil.scale(mPopView);
            mAddDevice = (LinearLayout) mPopView.findViewById(R.id.ll_add_device);
            mDeviceStyle = (LinearLayout) mPopView.findViewById(R.id.ll_device_type);
            mLlQrCode = (LinearLayout) mPopView.findViewById(R.id.ll_qr_code);
            mWifiScan = (LinearLayout) mPopView.findViewById(R.id.ll_wifi_scan);
            mPopupWindow = new PopupWindow(mPopView, SupportMultipleScreensUtil.getScaleValue(465),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true); // 设置是否获取焦点
        mPopupWindow.setTouchable(true); // 设置是否能否触摸
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mDataList = new ArrayList<>();
        GridLayoutManager mManagerLayout = new GridLayoutManager(getActivity(), 2);
        mRecyview.setLayoutManager(mManagerLayout);
        mAdapter = new HomeListAdapter(getActivity(), mDataList);
        mAdapter.setOnItemClickListener(this);
        mRecyview.setAdapter(mAdapter);
        requestQueryDevice();
    }

    @Override
    public void onItemClick(int position) {
        //type:1 灯 2 遥控设备
        if (mDataList.get(position).getType().equals("1")) {
            Intent it = new Intent(getActivity(), LightControl1Activity.class);
            it.putExtra("mac", mDataList.get(position).getMac());
            startActivity(it);
        }else if (mDataList.get(position).getType().equals("2")) {
            Intent itStb = new Intent(getActivity(), AcActivity.class);
            itStb.putExtra("brandId", mDataList.get(position).getBrandid());
            itStb.putExtra("id", mDataList.get(position).getCategoryid());
            itStb.putExtra("name", mDataList.get(position).getName());
            itStb.putExtra("device", mDataList.get(position).getIr_devicename());
            startActivity(itStb);
        }else if (mDataList.get(position).getType().equals("3")) {
            Intent it = new Intent(getActivity(), SocketControlActivity.class);
            it.putExtra("mac", mDataList.get(position).getMac());
            startActivity(it);
        }
    }

    private void initLitener() {
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopIsShow = true;

                ObjectAnimator ra = ObjectAnimator.ofFloat(mIvAdd, "rotation", 50f, 0f);
                ra.setDuration(300);
                ra.start();
            }
        });

        mAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ScanConnectActivity.class);

                mPopupWindow.dismiss();
            }
        });

        mDeviceStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("功能暂未开放");
                mPopupWindow.dismiss();
            }
        });

        mLlQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 二维码扫码
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, Constants.REQ_QR_CODE);
                mPopupWindow.dismiss();
            }
        });

        mWifiScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(SelecteTypeActivity.class);
                mPopupWindow.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constants.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constants.INTENT_EXTRA_KEY_QR_SCAN);
            Log.i(TAG, "scanResult = " + scanResult);
        }
    }


    //-------------------------------------------申请权限-----------------------------------------

    private void checkPermission() {
        BaseActivity.requestRuntimePermission(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
        }, new PermissionListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                //如果定位权限没开启，则城市名传默认的
                IOSAlertDialog iosAlertDialog = new IOSAlertDialog(getActivity());
                iosAlertDialog.builder().setTitle("温馨提示").setMessage("缺少定位权限,将导致蓝牙功能无法使用")
                        .setNegativeButton("取消", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                            }

                        }).setPositiveButton("去设置", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startAppSettings();
                    }
                }).show();
            }
        });
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(intent, CODE);
    }
}