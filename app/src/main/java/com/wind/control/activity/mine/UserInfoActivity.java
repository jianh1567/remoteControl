package com.wind.control.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.wind.control.R;
import com.wind.control.base.BaseActivity;
import com.wind.control.model.DeviceListBean;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.okhttp.request.HeadImgRequest;
import com.wind.control.okhttp.request.UserInfoRequest;
import com.wind.control.okhttp.utils.Base64Utils;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.ClickUtils;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.NetUtil;
import com.wind.control.view.CircleImageView;
import com.wind.control.view.IOSAlertDialog;
import com.wind.control.widget.SelectPicPopupWindow;
import com.wind.control.widget.timeselector.TimeSelector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener, TimeSelector.ResultHandler {

    @BindView(R.id.iv_user_hd)
    CircleImageView iv_user_hd;
    @BindView(R.id.tv_phone_num)
    TextView tv_phone_num;
    @BindView(R.id.tv_account_id)
    TextView tv_account_id;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_pay_pwd)
    TextView tv_pay_pwd;
    @BindView(R.id.tv_title_center)
    TextView mTvTitleCenter;
    private TimeSelector timeSelector;
    private SelectPicPopupWindow sexWindow;
    private SelectPicPopupWindow photoWindow;
    private String mBirthday;
    private String mSex;
    private String mHeadportrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mTvTitleCenter.setText("个人信息");
        tv_phone_num.setText(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        mBirthday = getIntent().getStringExtra("birthday");
        mSex = getIntent().getStringExtra("sex");
        mHeadportrait = getIntent().getStringExtra("headportrait");
        if (!TextUtils.isEmpty(mSex)) {
            tv_sex.setText(mSex);
        }

        if (!TextUtils.isEmpty(mBirthday)) {
            tv_birthday.setText(mBirthday);
            setResultBirthDay("1994-06-24");
        } else {
            setResultBirthDay("1994-06-24");
        }

        Glide.with(this).load(mHeadportrait)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_my_hd_def).error(R.drawable.ic_my_hd_def))
                .into(iv_user_hd);
    }

    /**
     * 处理日期选择控件
     *
     * @param birthDay
     */
    private void setResultBirthDay(String birthDay) {
        String year;
        String mouth;
        String day;
        if (TextUtils.isEmpty(birthDay)) {
            year = "";
            mouth = "";
            day = "";
        } else {
            String[] date = birthDay.split("-");
            year = date[0];
            mouth = date[1];
            day = date[2];
        }

        timeSelector = new TimeSelector(this, this, year, mouth, day);
        timeSelector.setMode(TimeSelector.MODE.YMD);
    }

    @OnClick({R.id.ll_btn_hd_photo, R.id.ll_btn_phone_num, R.id.ll_btn_account_id, R.id.ll_btn_sex, R.id.ll_btn_birthday,
            R.id.ll_btn_login_pwd, R.id.ll_btn_pay_pwd, R.id.tv_btn_logout, R.id.iv_left})
    public void onClick(View view) {
        if (ClickUtils.isContinual()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.ll_btn_hd_photo:
                photoWindow = new SelectPicPopupWindow(this, this, 1);
                photoWindow.update();
                photoWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_btn_phone_num:
                break;
            case R.id.ll_btn_account_id:
                break;
            case R.id.ll_btn_sex:
                sexWindow = new SelectPicPopupWindow(this, this, 2);
                sexWindow.update();
                sexWindow.showAtLocation(this.findViewById(android.R.id.content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ll_btn_birthday:
                timeSelector.show();
                break;
            case R.id.ll_btn_login_pwd:

                break;
            case R.id.ll_btn_pay_pwd:

                break;
            case R.id.tv_btn_logout:
                logout();
                break;
            case R.id.rv_photo_paizhao:
                photoWindow.dismiss();
                requestCamera();

                break;
            case R.id.rv_photo_xcxz:
                photoWindow.dismiss();
                requestCrop();

                break;
            case R.id.rv_sex_boy:
                tv_sex.setText(R.string.male);
                requestUpdateInfo(1, "", "男");
                sexWindow.dismiss();
                break;
            case R.id.rv_sex_gril:
                tv_sex.setText(R.string.female);
                requestUpdateInfo(1, "", "女");
                sexWindow.dismiss();
                break;
        }
    }

    @Override
    public void handle(String time) {
        String mBirthDay = time.substring(0, 10);
        tv_birthday.setText(mBirthDay);
        requestUpdateInfo(2, mBirthDay, "");
    }

    /**
     * 退出登录
     */
    private void logout() {
        //退出登录
        IOSAlertDialog iosAlertDialog = new IOSAlertDialog(this);
        iosAlertDialog.builder().setTitle("温馨提示").setMessage("是否确认退出登录!")
                .setNegativeButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (ClickUtils.isContinual()) {
                            return;
                        }

                        BaseInfoSPUtil.getInstance().removeSpData(UserInfoActivity.this, BaseInfoSPUtil.KEY_LOGIN_TOKEN);
                        finish();
                    }

                }).setPositiveButton("取消", new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    //****************************************相机、拍照、裁剪部分*******************************************
    //从相册选取
    private void requestCrop() {

        PhotoPicker.builder()
                .setPhotoCount(1)
                .setPreviewEnabled(false)
                .setCrop(true)
                .setCropXY(1, 1)
                .setCropColors(R.color.black, R.color.black)
                .start(this);

    }

    //拍照
    private void requestCamera() {
        PhotoPicker.builder()
                .setOpenCamera(true)
                .setCrop(true)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照功能或者裁剪功能返回
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.CROP_CODE) {
            try {
                String localUrl = data.getStringExtra(PhotoPicker.KEY_CAMEAR_PATH);
                Bitmap bitmap = BitmapFactory.decodeFile(localUrl);
                iv_user_hd.setImageBitmap(bitmap);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                String hd = Base64Utils.encodeToString(bos.toByteArray(), Base64.NO_WRAP);
                requestUpdateHeadImg(hd);
            } catch (IOException e) {

            }
        }
    }

    //修改用户信息
    private void requestUpdateInfo(int type, String birthday, String sex) {
        UserInfoRequest request = new UserInfoRequest();
        request.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        request.setToken(BaseInfoSPUtil.getInstance().getLoginToken(this));
        request.setType(type);
        if (type == 1) {
            request.setSex(sex);
        } else if (type == 2) {
            request.setBirthday(birthday);
        }
        OkHttpUtils.postString().url(Api.UPDATE_USER_INFO)
                .content(new Gson().toJson(request))
                .build()
                .execute(new GenericsCallback<DeviceListBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(UserInfoActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(DeviceListBean response, int id, int code) {
                        String mCode = response.getCode();
                        if (mCode.equals("1000")) {
                            showToast(response.getMsg());
                        } else {
                            showToast(response.getMsg());
                        }
                    }
                });
    }

    //修改头像
    private void requestUpdateHeadImg(String headImg) {
        HeadImgRequest request = new HeadImgRequest();
        request.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        request.setToken(BaseInfoSPUtil.getInstance().getLoginToken(this));
        request.setUserImg(headImg);
        OkHttpUtils.postString().url(Api.UPDATE_PROFILE_PICTURE)
                .content(new Gson().toJson(request))
                .build()
                .execute(new GenericsCallback<DeviceListBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(UserInfoActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(DeviceListBean response, int id, int code) {
                        String mCode = response.getCode();
                        if (mCode.equals("1000")) {
                            showToast("头像修改成功");
                        } else {
                            showToast(response.getMsg());
                        }
                    }
                });
    }

}