package com.wind.control.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wind.control.R;
import com.wind.control.adapter.SelectPhotosAdapter;
import com.wind.control.base.BaseActivity;
import com.wind.control.dialog.LoadingDialog;
import com.wind.control.model.DeviceListBean;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.okhttp.request.SubmitFeedbackRequest;
import com.wind.control.okhttp.utils.Base64Utils;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.CheckUtils;
import com.wind.control.util.ClickUtils;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.NetUtil;
import com.wind.control.util.OnItemClickLisnter;
import com.wind.control.util.ToastUtils;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;

public class FeedbackActivity extends BaseActivity implements OnItemClickLisnter, TextWatcher {

    @BindView(R.id.et_feedback_tittle)
    EditText et_feedback_tittle;
    @BindView(R.id.et_feedback_desc)
    EditText et_feedback_desc;
    @BindView(R.id.tv_surplus)
    TextView tv_surplus;
    @BindView(R.id.rv_photos)
    RecyclerView rv_photos;
    @BindView(R.id.et_feedback_phone_num)
    EditText et_feedback_phone_num;
    private SelectPhotosAdapter mSelectPhotosAdapter;//图片适配器
    private List<String> mUrls;//本地图片地址集合
    //网络加载框
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initData();
        initViews();
        showPhotos();
    }

    private void initData() {
        mUrls = new ArrayList<>();
        mSelectPhotosAdapter = new SelectPhotosAdapter(this);
        mSelectPhotosAdapter.setOnItemClickLisnter(this);
        //初始化LoadingDialog
        mLoadingDialog = new LoadingDialog(this, R.style.LoadingDialog, "正在提交...");
    }

    private void initViews() {

        GridLayoutManager gm = new GridLayoutManager(this, 4);
        rv_photos.setLayoutManager(gm);
        rv_photos.addItemDecoration(new GridSpacingItemDecoration(4, SupportMultipleScreensUtil.getScaleValue(20), true));
        rv_photos.setAdapter(mSelectPhotosAdapter);
        rv_photos.setNestedScrollingEnabled(false);

        et_feedback_desc.addTextChangedListener(this);
    }

    private void showSuccessDialog() {
//        OperateStatusDialog.init(this)
//                .title(getString(R.string.successful_feedback_submission))
//                .operateDesc(R.string.confirm_back)
//                .mark(R.drawable.ic_my_feedback_success)
//                .operated(new OperateStatusDialog.OnOperatedListener() {
//                    @Override
//                    public void onOperated(Dialog dialog) {
//                        finish();
//                    }
//                })
//                .show();
    }

    /**
     * 显示图片
     */
    private void showPhotos() {
        mSelectPhotosAdapter.setData(mUrls);
    }

    private void submitFeedback(final String tittle, final String desc, final String phone_num) {

        mLoadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                SubmitFeedbackRequest request = new SubmitFeedbackRequest();
                request.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(FeedbackActivity.this));
                request.setUserLink(phone_num);
                request.setPbDescription(desc);
                request.setUserTitle(tittle);
                request.setToken(BaseInfoSPUtil.getInstance().getLoginToken(FeedbackActivity.this));
                List<SubmitFeedbackRequest.UserImgBean> imgList = new ArrayList<>();
                try {
                    for (int i = 0, size = mUrls.size(); i < size; i++) {
                        Bitmap bitmap = BitmapFactory.decodeFile(mUrls.get(i));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int quality = 90;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                        while (baos.toByteArray().length / 1024 > 200 && quality > 5) {
                            baos.reset();
                            quality -= 5;
                            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                        }
                        baos.flush();
                        baos.close();
                        String img = Base64Utils.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
                        SubmitFeedbackRequest.UserImgBean bean = new SubmitFeedbackRequest.UserImgBean();
                        bean.setImgStream(img);
                        imgList.add(bean);

                    }
                } catch (IOException e) {
                    mLoadingDialog.dismiss();
                    return;
                }
                request.setUserImg(imgList);

                OkHttpUtils.postString().url(Api.OP_FEED_BACK)
                        .content(new Gson().toJson(request))
                        .build()
                        .execute(new GenericsCallback<DeviceListBean>(new JsonGenericsSerializator()) {
                            @Override
                            public void onError(Call call, Exception e, int id, int code) {
                                if (!NetUtil.isNetworkAvailable(FeedbackActivity.this)) {
                                    showToast(getResources().getString(R.string.isNetWork));
                                }
                            }

                            @Override
                            public void onResponse(DeviceListBean response, int id, int code) {
                                String mCode = response.getCode();
                                if (mCode.equals("1000")) {
                                    ToastUtils.showToast(response.getMsg());
                                    mLoadingDialog.dismiss();
                                    finish();
                                } else {
                                    mLoadingDialog.dismiss();
                                    showToast(response.getMsg());
                                }
                            }
                        });
            }
        }).start();
    }

    private int mSelectedPosition = 0;

    @Override
    public void onItemClick(View view, int position, Object data) {
        if (ClickUtils.isContinual()) {
            return;
        }
        mSelectedPosition = position;
        int size = mUrls.size();
        int count = 1;
        if (position == size) {
            count = 6 - size;
        }
        PhotoPicker.builder()
                .setPhotoCount(count)
                .setPreviewEnabled(false)
                .setCrop(false)
                .setCropColors(R.color.black, R.color.black)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相册返回
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            List<String> urls = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            if ((mSelectedPosition < mUrls.size())) {
                mUrls.remove(mSelectedPosition);
            }
            mUrls.addAll(mSelectedPosition, urls);
            showPhotos();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        tv_surplus.setText((200 - s.length()) + "/200");
    }

    @OnClick({R.id.iv_left, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_submit:
                String tittle = et_feedback_tittle.getText().toString().trim();
                String desc = et_feedback_desc.getText().toString();
                String phone_num = et_feedback_phone_num.getText().toString();

                if (TextUtils.isEmpty(tittle)) {
                    ToastUtils.showToast(getResources().getString(R.string.please_input_title_content));
                } else if (TextUtils.isEmpty(desc)) {
                    ToastUtils.showToast(getResources().getString(R.string.please_input_feedback_content));
                } else if (TextUtils.isEmpty(phone_num)) {
                    ToastUtils.showToast("请输入手机号");
                }else if(!CheckUtils.isPhoneNum(phone_num)){
                    ToastUtils.showToast("请填写正确的手机号");
                } else {
                    submitFeedback(tittle, desc, phone_num);
                }

                break;
        }
    }

    /**
     * item间距
     */

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
}
