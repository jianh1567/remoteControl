package com.wind.control.activity.load;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wind.control.R;
import com.wind.control.base.BaseActivity;
import com.wind.control.dialog.LoadingDialog;
import com.wind.control.model.LoginBean;
import com.wind.control.model.LoginCodeBean;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.okhttp.service.GetCodeRequest;
import com.wind.control.okhttp.service.LoginRequest;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.CheckUtils;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.NetUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by houjian on 2018/3/19.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_login_pwd)
    TextView mTvLoginPwd;
    @BindView(R.id.tv_login_code)
    TextView mTvLoginCode;
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    @BindView(R.id.view_line_left)
    ImageView mViewLineLeft;
    @BindView(R.id.view_line_right)
    ImageView mViewLineRight;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.ll_login_pwd)
    LinearLayout mLlLoginPwd;
    @BindView(R.id.ll_login_code)
    LinearLayout mLlLoginCode;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_code)
    Button mBtnCode;

    //tab下划线动画
    private Animation mAnimRight;
    private Animation mAnimLeft;

    //是否点击密码登录
    private boolean isCilckPwd = false;
    //是否点击验证码登录
    private boolean isClickCode = true;
    //是否是验证码登录
    private boolean isLoginCode = true;
    private String mPhoneNum;
    private String mPwd;
    private String mCode;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoadingDialog = new LoadingDialog(this, R.style.LoadingDialog, "登录中...");
        initView();
        initAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取手机号码
        mEtPhone.setText(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
    }

    private void initView() {
        SpannableString spannableString = new SpannableString(mTvRegister.getText().toString());
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.btn_bg_color));
        spannableString.setSpan(colorSpan, 8, 11, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvRegister.setText(spannableString);
    }

    @OnClick({R.id.btn_login, R.id.tv_login_pwd, R.id.tv_login_code,
            R.id.tv_forget_pwd, R.id.tv_register, R.id.btn_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                hideInputMethod();
                login();
                break;
            case R.id.tv_login_pwd:
                if (isCilckPwd) {
                    isCilckPwd = false;
                    isClickCode = true;
                    mViewLineRight.startAnimation(mAnimRight);
                }
                break;
            case R.id.tv_login_code:
                if (isClickCode) {
                    isCilckPwd = true;
                    isClickCode = false;
                    mViewLineLeft.startAnimation(mAnimLeft);
                }
                break;
            case R.id.tv_forget_pwd:
                hideInputMethod();
                //   openActivity(ForgetPwdActivity.class);
                break;
            case R.id.tv_register:
                hideInputMethod();
                openActivity(RegisterActivity.class);
                break;
            case R.id.btn_code:
                hideInputMethod();
                prepGetVerifyCode();
                break;
        }
    }

    //处理登录相关
    private void login() {

        mPhoneNum = mEtPhone.getText().toString();
        mPwd = mEtPwd.getText().toString();
        mCode = mEtCode.getText().toString();

        if (TextUtils.isEmpty(mPhoneNum)) {
            showToast(getString(R.string.et_input_phone));
            return;
        }

        if (!CheckUtils.isPhoneNum(mPhoneNum)) {
            showToast(getString(R.string.txt_error_phone_num));
            return;
        }

        //是否是验证码登录
        if (isLoginCode) {

            if (TextUtils.isEmpty(mCode)) {
                showToast(getString(R.string.txt_input_code));
                return;
            }

        } else {

            if (TextUtils.isEmpty(mPwd)) {
                showToast(getString(R.string.input_pwd_hint));
                return;
            }

            if (!CheckUtils.isPassword(mPwd)) {
                showToast(getString(R.string.txt_error_pwd));
                return;
            }

        }
        requestLogin();
    }

    //倒计时相关
    private void prepGetVerifyCode() {
        mPhoneNum = mEtPhone.getText().toString();
        if (TextUtils.isEmpty(mPhoneNum)) {
            showToast(getString(R.string.et_input_phone));
        } else if (!CheckUtils.isPhoneNum(mPhoneNum)) {
            showToast(getString(R.string.txt_error_phone_num));
        } else {
            mBtnCode.setEnabled(false);
            CountDownTimer timer = new CountDownTimer(60000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    mBtnCode.setText("重新发送" + "(" + millisUntilFinished / 1000 + ")");
                }

                @Override
                public void onFinish() {
                    mBtnCode.setEnabled(true);
                    mBtnCode.setText("获取验证码");
                }
            };
            timer.start();
            requestVerifyCode();
        }
    }

    //----------------------------------------------网络相关----------------------------------------

    /**
     * 密码或验证码请求登录
     **/
    private void requestLogin() {
        mLoadingDialog.show();
        String login;
        LoginRequest bean = new LoginRequest();
        bean.setPhone(mPhoneNum);
        if (isLoginCode) {
            login = Api.OTHER_LOGIN;
            bean.setIdentifyCode(mCode);
        } else {
            login = Api.LOGIN;
            bean.setPassword(mPwd);
        }
        OkHttpUtils.postString()
                .url(login)
                .content(new Gson().toJson(bean))
                .build()
                .execute(new GenericsCallback<LoginBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        mLoadingDialog.dismiss();
                        if (!NetUtil.isNetworkAvailable(LoginActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(LoginBean response, int id, int code) {
                        int mCode = response.getCode();
                        if (mCode == 1000) {
                            BaseInfoSPUtil.getInstance().setLoginToken(LoginActivity.this, response.getToken());
                            showToast(response.getMsg());
                            //存储用户手机号码
                            BaseInfoSPUtil.getInstance().setUserPhoneNum(LoginActivity.this, mPhoneNum);
                            finish();
                        } else {
                            showToast(response.getMsg());
                        }
                        mLoadingDialog.dismiss();
                    }
                });
    }

    /**
     * 发送验证码
     **/
    private void requestVerifyCode() {
        GetCodeRequest bean = new GetCodeRequest();
        bean.setPhone(mPhoneNum);
        OkHttpUtils.postString()
                .url(Api.GET_LOGIN_PHONE_CODE)
                .content(new Gson().toJson(bean))
                .build()
                .execute(new GenericsCallback<LoginCodeBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(LoginActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(LoginCodeBean response, int id, int code) {

                        String mCode = response.getCode();
                        if (mCode.equals("1000")) {
                            showToast("发送成功");
                        } else {
                            //showToast(response.get());
                        }
                    }
                });
    }

    //------------------------------------------tab线向左向右平移动画---------------------------------

    /***
     * 初始化动画
     */
    private void initAnimation() {
        /* 线左边移动 */
        mAnimRight = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.view_line_move_left);
        mAnimRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //   isLoginCode = false;
                mTvLoginPwd.setTextColor(getResources().getColor(R.color.white));
                mTvLoginCode.setTextColor(getResources().getColor(R.color.txt_login_hint));
                mViewLineLeft.setVisibility(View.VISIBLE);
                mViewLineRight.setVisibility(View.INVISIBLE);
                mLlLoginPwd.setVisibility(View.VISIBLE);
                mLlLoginCode.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        /* 线右边移动 */
        mAnimLeft = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.view_line_move_right);
        mAnimLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //      isLoginCode = true;
                mTvLoginPwd.setTextColor(getResources().getColor(R.color.txt_login_hint));
                mTvLoginCode.setTextColor(getResources().getColor(R.color.white));
                mViewLineLeft.setVisibility(View.INVISIBLE);
                mViewLineRight.setVisibility(View.VISIBLE);
                mLlLoginPwd.setVisibility(View.GONE);
                mLlLoginCode.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}