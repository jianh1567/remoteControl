package com.wind.control.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.wind.control.R;
import com.wind.control.activity.MySceneActivity;
import com.wind.control.activity.device.AddDeviceActivity;
import com.wind.control.activity.device.BindDeviceActivity;
import com.wind.control.activity.mine.AboutUsActivity;
import com.wind.control.activity.mine.FeedbackActivity;
import com.wind.control.activity.mine.UserInfoActivity;
import com.wind.control.appupdate.AppUpdateDialog;
import com.wind.control.base.BaseFragment;
import com.wind.control.model.UserInfoBean;
import com.wind.control.model.VersionInfoBean;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.FileCallBack;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.okhttp.request.RequestCall;
import com.wind.control.okhttp.service.QueryDeviceRequest;
import com.wind.control.util.AppInfoUtils;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.Constants;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.NetUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/7/9
 * 注释：我的
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.rl_scene)
    RelativeLayout mRlScene;
    @BindView(R.id.rl_my_device)
    RelativeLayout mRlMyDevice;
    @BindView(R.id.rl_setting)
    RelativeLayout mRlSetting;
    @BindView(R.id.rl_feedback)
    RelativeLayout mRlFeedback;
    @BindView(R.id.rl_about)
    RelativeLayout mRlAbout;
    @BindView(R.id.rl_bind_device)
    RelativeLayout mRlBindDevice;
    @BindView(R.id.ll_header)
    LinearLayout mLlHeader;
    @BindView(R.id.ll_one)
    LinearLayout mLlOne;
    @BindView(R.id.iv_header)
    ImageView mIvHeader;
    @BindView(R.id.rl_update_app)
    RelativeLayout mRlUpdateApp;
    Unbinder unbinder;
    private String mBirthday;
    private String mSex;
    private String mHeadportrait;
    private boolean isHidden;
    private AppUpdateDialog mAppUpdateDialog;
    private boolean mStopDownload = false;
    private static final String TAG = "MineFragment";
    private RequestCall mDownLoadRequest;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isHidden = false;
            if (TextUtils.isEmpty(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))) {
                mLlOne.setVisibility(View.GONE);
                mTvPhone.setText("游客");
                Glide.with(getActivity()).load("")
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.ic_my_hd_def).error(R.drawable.ic_my_hd_def))
                        .into(mIvHeader);
            } else {
                mLlOne.setVisibility(View.VISIBLE);
                requeatGetUserInfo();
            }
        } else {
            isHidden = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (TextUtils.isEmpty(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))) {
            mLlOne.setVisibility(View.GONE);
            Glide.with(getActivity()).load("")
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.ic_my_hd_def).error(R.drawable.ic_my_hd_def))
                    .into(mIvHeader);
            mTvPhone.setText("游客");
        } else {
            mLlOne.setVisibility(View.VISIBLE);
            if (isHidden) {
                return;
            }
            requeatGetUserInfo();
        }
    }

    @OnClick({R.id.rl_scene, R.id.rl_my_device, R.id.rl_setting,
            R.id.rl_feedback, R.id.rl_about, R.id.ll_header,
            R.id.rl_bind_device, R.id.rl_update_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_scene:
                openActivity(MySceneActivity.class);
                break;
            case R.id.rl_my_device:
                openActivity(AddDeviceActivity.class);
                break;
            case R.id.rl_setting:
                break;
            case R.id.rl_feedback:
                openActivity(FeedbackActivity.class);
                break;
            case R.id.rl_about:
                openActivity(AboutUsActivity.class);
                break;
            case R.id.ll_header:
                if (TextUtils.isEmpty(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()))) {
                    return;
                }
                Intent it = new Intent();
                it.putExtra("birthday", mBirthday);
                it.putExtra("sex", mSex);
                it.putExtra("headportrait", mHeadportrait);
                openActivity(UserInfoActivity.class, it);
                break;
            case R.id.rl_bind_device:
                openActivity(BindDeviceActivity.class);
                break;
            case R.id.rl_update_app:
                 requestUpdateApp();
        }
    }

    private void requestUpdateApp(){
        OkHttpUtils.post()
                .url(Api.VERSION)
                .build()
                .execute(new GenericsCallback<VersionInfoBean>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if(!NetUtil.isNetworkAvailable(getActivity())){
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(VersionInfoBean response, int id, int code) {
                        int mCode = response.getResultCode();
                        final VersionInfoBean.VersionInfo versionInfo = response.getVersionInfo();
                        if(mCode == 1000){
                            if(!versionInfo.getVersion()
                                    .equals(AppInfoUtils.getVersionName(getActivity()))){
                                String[] content = versionInfo.getContent().split(";");

                                String updateContent = "";
                                for(int i = 0; i < content.length; i++){
                                    if(i == content.length - 1){
                                        updateContent = updateContent + content[i];
                                    }else {
                                        updateContent = updateContent + content[i] + "\n";
                                    }
                                }

                                mAppUpdateDialog = new AppUpdateDialog(getActivity(), "更新应用",
                                        updateContent, "更新应用", "取消更新", 1);

                                mAppUpdateDialog.setClickListener(new AppUpdateDialog.BtnClickListener() {
                                    @Override
                                    public void doConfirm() {
                                        mStopDownload = false;
                                        requestDownloadFile(versionInfo.getName());
                                    }

                                    @Override
                                    public void doCancel() {
                                        mAppUpdateDialog.cancel();
                                    }

                                    @Override
                                    public void doNoDown() {
                                        Log.i(TAG, "doNoDown ");
                                        if(mDownLoadRequest != null){
                                            mDownLoadRequest.cancel();
                                        }

                                        mAppUpdateDialog.cancel();
                                        mStopDownload = true;
                                    }
                                });

                                mAppUpdateDialog.show();
                            }else {
                                showToast("已是最新版本");
                            }
                        }else {

                        }

                    }
                });
    }

    private void requestDownloadFile(String pkgName){
        mDownLoadRequest = OkHttpUtils.get()
                .url(Api.PACKAGE)
                .addParams("parm", pkgName)
                .build();

        mDownLoadRequest.execute(new FileCallBack(Constants.SDCARD_PATH, Constants.APP_NAME) {
                   @Override
                   public void inProgress(float progress, long total, int id) {
                        if(!mStopDownload){
                            int point = (int) (progress * 100);
                            mAppUpdateDialog.UpdateProgress("更新中", point + "%", "确定", "取消",
                                    point);
                            if(point == 100){
                                mAppUpdateDialog.dismiss();
                                File file = new File(Constants.SDCARD_PATH, Constants.APP_NAME);
                                if(file != null){
                                    installApk(file);
                                }
                            }
                        }
                   }

                   @Override
                   public void onError(Call call, Exception e, int id, int code) {

                   }

                   @Override
                   public void onResponse(File response, int id, int code) {

                   }
               });
    }

    private void installApk(File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            Uri apkUri =
                    FileProvider.getUriForFile(getActivity(), "com.wind.control.provider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    private void requeatGetUserInfo() {
        QueryDeviceRequest bean = new QueryDeviceRequest();
        bean.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(getActivity()));
        bean.setToken(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()));
        OkHttpUtils.postString().url(Api.GET_USER_INFO)
                .content(new Gson().toJson(bean))
                .build()
                .execute(new GenericsCallback<UserInfoBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(getActivity())) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(UserInfoBean response, int id, int code) {
                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            mBirthday = response.getUserinfo().getBirthday();
                            mSex = response.getUserinfo().getSex();
                            mHeadportrait = response.getUserinfo().getHeadportrait();
                            mTvPhone.setText(response.getUserinfo().getUsername());
                            Glide.with(getActivity()).load(response.getUserinfo().getHeadportrait())
                                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                                            .placeholder(R.drawable.ic_my_hd_def).error(R.drawable.ic_my_hd_def))
                                    .into(mIvHeader);
                        } else {
                            //    showToast(response.getMsg());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}