package com.wind.control.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.wind.control.adapter.DeviceListAdapter;
import com.wind.control.base.BaseFragment;
import com.wind.control.model.DelDeviceBean;
import com.wind.control.model.DeviceListBean;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.okhttp.service.QueryDeviceRequest;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.Constants;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.NetUtil;
import com.wind.control.widget.SupportMultipleScreensUtil;
import com.wind.control.widget.listview.XSwipeMenuListView;
import com.wind.control.widget.swipemenulistview.SwipeMenu;
import com.wind.control.widget.swipemenulistview.SwipeMenuCreator;
import com.wind.control.widget.swipemenulistview.SwipeMenuItem;
import com.wind.control.widget.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/8/21
 * 注释：设备列表
 */
public class DeviceFragment extends BaseFragment implements AdapterView.OnItemClickListener, XSwipeMenuListView.IXListViewListener {

    @BindView(R.id.tv_title_center)
    TextView mTvTitleCenter;
    @BindView(R.id.iv_add)
    ImageView mIvAdd;
    @BindView(R.id.rl_title_content)
    RelativeLayout mRlTitleContent;
    @BindView(R.id.device_list)
    XSwipeMenuListView mDeviceList;

    private View mPopView;
    private PopupWindow mPopupWindow;
    private boolean mPopIsShow = true;
    private LinearLayout mAddDevice;
    private LinearLayout mDeviceStyle;
    private LinearLayout mLlQrCode;
    private LinearLayout mWifiScan;
    private DeviceListAdapter mAdapter;
    private List<DeviceListBean.DevicesBean> mDataList;
    private boolean isHidden;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        if (TextUtils.isEmpty(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))){
            mDeviceList.setVisibility(View.GONE);
            return;
        }
        mDeviceList.setVisibility(View.VISIBLE);
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
                        mDataList.clear();
                        String mCode = response.getCode();
                        if (mCode.equals("1000")) {
                            mDeviceList.stopRefresh();
                            mDataList.addAll(response.getDevices());
                            mAdapter.notifyDataSetChanged();
                        } else {
                         //   showToast(response.getMsg());
                        }
                    }
                });
    }

    private void requestDeviceDevice(int id) {
        DelDeviceBean request = new DelDeviceBean();
        request.setId(id);
        request.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(getActivity()));
        request.setToken(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()));
        OkHttpUtils.postString()
                .url(Api.DELETE_DEVICE)
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
                        if (mCode.equals("1000")) {
                            requestQueryDevice();
                        } else {
                            showToast(response.getMsg());
                        }
                    }
                });
    }


    @OnClick({R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                if (!mPopIsShow) {
                    mPopIsShow = true;
                    mPopupWindow.dismiss();
                } else {
                    ObjectAnimator ra = ObjectAnimator.ofFloat(mIvAdd, "rotation", 0f, 50f);
                    ra.setDuration(300);
                    ra.start();
                    mPopIsShow = false;
                    mPopupWindow.showAsDropDown(mRlTitleContent, SupportMultipleScreensUtil.getScaleValue(595), 0);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //type:1 灯 2 遥控设备
        if (mDataList.get(position - 1).getType().equals("1")) {
            Intent it = new Intent(getActivity(), LightControl1Activity.class);
            it.putExtra("mac", mDataList.get(position - 1).getMac());
            startActivity(it);
        } else if(mDataList.get(position - 1).getType().equals("2")){
            Intent itStb = new Intent(getActivity(), AcActivity.class);
            itStb.putExtra("brandId", mDataList.get(position-1).getBrandid());
            itStb.putExtra("id", mDataList.get(position-1).getCategoryid());
            itStb.putExtra("name", mDataList.get(position-1).getName());
            itStb.putExtra("device", mDataList.get(position-1).getIr_devicename());
            startActivity(itStb);
        }else if(mDataList.get(position - 1).getType().equals("3")){
            Intent it = new Intent(getActivity(), SocketControlActivity.class);
            it.putExtra("mac", mDataList.get(position - 1).getMac());
            startActivity(it);
        }
    }

    @Override
    public void onRefresh() {
        requestQueryDevice();
    }

    @Override
    public void onLoadMore() {

    }

    private void initView() {

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "open" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(SupportMultipleScreensUtil.getScaleValue(260));
                // set item title
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);

            }
        };

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
        mDeviceList.setOnItemClickListener(this);
        mDeviceList.setPullRefreshEnable(true);
        mDeviceList.setPullLoadEnable(false);
        //刷新和加载更多
        mDeviceList.setXListViewListener(this);
        mDataList = new ArrayList<>();
        mAdapter = new DeviceListAdapter(getActivity(), mDataList);
        mDeviceList.setAdapter(mAdapter);
        // set creator
        mDeviceList.setMenuCreator(creator);
        mDeviceList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        requestDeviceDevice(mDataList.get(position).getId());
                        //deleteCollestion(mScenicSpotList.get(position).getScenicId(), "scenic");
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

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

}