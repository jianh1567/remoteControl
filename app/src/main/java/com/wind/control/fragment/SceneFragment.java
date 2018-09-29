package com.wind.control.fragment;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.wind.control.R;
import com.wind.control.activity.SceneSettingActivity;
import com.wind.control.activity.load.LoginActivity;
import com.wind.control.adapter.SceneListAdapter;
import com.wind.control.base.BaseFragment;
import com.wind.control.manager.AircControlManager;
import com.wind.control.manager.LightControlManager;
import com.wind.control.model.DeleteSceneBean;
import com.wind.control.model.DeleteSceneResultBean;
import com.wind.control.model.SceneInfoQueryBean;
import com.wind.control.model.SceneInfoResultBean;
import com.wind.control.model.SceneInfoResultBean.SceneModel;
import com.wind.control.model.SceneList;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.ToastUtils;
import com.wind.control.view.LoginDialog;
import com.wind.control.widget.SupportMultipleScreensUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/7/9
 * 注释：发现
 */
public class SceneFragment extends BaseFragment {
    private static final String TAG = "SceneFragment";
   /* @BindView(R.id.tv_add_scene)
    TextView mTvAddScene;*/
    @BindView(R.id.iv_add_scene)
    ImageView mIvAddScene;
    @BindView(R.id.rl_title_content)
    RelativeLayout mRlTitleContent;

    Unbinder unbinder;
    @BindView(R.id.im_back)
    ImageView mImBack;
    private SceneListAdapter mSceneListAdapter;
    private ArrayList<SceneList> mSceneList = new ArrayList<>();

    private TwinklingRefreshLayout mTrlRefreshSceneView;

    private int mRefreshSceneStatus = 0;

    private View mPopView;
    private PopupWindow mPopupWindow;
    private boolean mPopIsShow = true;
    private LinearLayout mAddScene;
    private boolean mBackViewVisible;
    private LightControlManager mLightControlManager;
    private boolean mHasLogin;
    private boolean isHidden;
    private int mStartPage = 0;
    private int mLimitPage = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scene;
    }

    private int[] sceneImage = new int[]{R.drawable.scene_bedroom, R.drawable.scene_drawing_room, R.drawable.scene_toilet};

    private int[] sceneName =  new int[]{R.string.scene_bedroom, R.string.scene_drawing_room, R.string.scene_toilet};

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "sceneFragment onresume");
        super.onResume();
        if(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()).equals("")){
            mHasLogin = false;
        }else {
            mHasLogin = true;
        }

        if (!isHidden) {
            initData();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i(TAG, "sceneFragment onHiddenChanged");
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isHidden = false;
            initData();
        } else {
            isHidden = true;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLightControlManager = new LightControlManager(getActivity());
    }

    @OnClick({R.id.iv_add_scene, R.id.im_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_scene:
                if(!mHasLogin){
                    showLoginDialog();
                }else {
                    if (!mPopIsShow) {
                        mPopIsShow = true;
                        mPopupWindow.dismiss();
                    } else {
                        ObjectAnimator ra = ObjectAnimator.ofFloat(mIvAddScene, "rotation", 0f, 50f);
                        ra.setDuration(300);
                        ra.start();
                        mPopIsShow = false;
                        mPopupWindow.showAsDropDown(mRlTitleContent, SupportMultipleScreensUtil.getScaleValue(595), 0);
                    }
                }
                break;
            case R.id.im_back:
                getActivity().onBackPressed();
        }
    }

    public static synchronized SceneFragment getInstance() {
        SceneFragment sceneFragment = new SceneFragment();
        return sceneFragment;
    }

    private void initData(){
        if(mHasLogin){
            mRefreshSceneStatus = 0;
            getSceneDataFromServ();
        }else {
            getDataFromLocal();
        }
    }

    public void getSceneDataFromServ() {
        if(mRefreshSceneStatus == 0){
            mStartPage = 0;
            if(mSceneList.size() < 10){
                mLimitPage = 10;
            }else {
                mLimitPage = mSceneList.size(); //刷新已经加载的数据
            }
        }else if(mRefreshSceneStatus == 1){
            mStartPage = mSceneList.size(); //动态加载更多数据
            mLimitPage = mStartPage + 10;
        }

        SceneInfoQueryBean sceneInfoQueryBean = new SceneInfoQueryBean();
        sceneInfoQueryBean.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(getActivity()));
        sceneInfoQueryBean.setStartpage(mStartPage);
        sceneInfoQueryBean.setLimitpage(mLimitPage);
        sceneInfoQueryBean.setToken(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()));

        OkHttpUtils.postString()
                .url(Api.QUERY_SCENE_LIST)
                .content(new Gson().toJson(sceneInfoQueryBean))
                .build()
                .execute(new GenericsCallback<SceneInfoResultBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        Toast.makeText(getActivity(), "访问网络错误 code = " + code, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(SceneInfoResultBean response, int id, int code) {
                        int responseCode = response.getCode();
                        Log.i(TAG, "getSceneDataFromServ responseCode = " + responseCode);
                        String result = new Gson().toJson(response);
                        Log.i(TAG, "result = " + result);
                        if (responseCode == 1000) {
                            SceneModel[] sceneModels = response.getSceneModel();
                            Log.i(TAG, "sceneModels.length = " + sceneModels.length);
                            if (sceneModels.length != 0) {
                                getDataFromServ(sceneModels);
                            }
                        }else if(responseCode == 1002){
                            ToastUtils.showToast(getString(R.string.no_more_data));
                        }
                    }
                });
    }

    private void getDataFromLocal() {
        mSceneList.clear();

        for (int i = 0; i < 3; i++) {
            SceneList sceneList = new SceneList();
            sceneList.setImageId(sceneImage[i]);
            sceneList.setSceneName(getString(sceneName[i]));
            sceneList.setDelBtnVisible(false);
            mSceneList.add(sceneList);
        }

        mSceneListAdapter.notifyDataSetChanged();
    }

    private void getDataFromServ(SceneModel[] sceneModels) {
        if(mRefreshSceneStatus == 0){
            mSceneList.clear();
        }

        int sceneImageId = 0;
        for (int i = 0; i < sceneModels.length; i++) {
            if (sceneImageId >= 3) {
                sceneImageId = 0;
            }
            SceneList sceneList = new SceneList();
            sceneList.setImageId(sceneImage[sceneImageId++]);
            sceneList.setSceneName(sceneModels[i].getScenename());
            sceneList.setSceneModel(sceneModels[i]);
            mSceneList.add(sceneList);
        }
        Log.i(TAG, "mSceneList.size = " + mSceneList.size());
        mSceneListAdapter.notifyDataSetChanged();
    }

    private void deleteSceneInServDB(int sceneId, final int position) {
        DeleteSceneBean deleteSceneBean = new DeleteSceneBean();
        deleteSceneBean.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(getActivity()));
        deleteSceneBean.setScenemodel_id(sceneId);
        deleteSceneBean.setToken(BaseInfoSPUtil.getInstance().getLoginToken(getActivity()));

        OkHttpUtils.postString()
                .url(Api.DELETE_SCENE)
                .content(new Gson().toJson(deleteSceneBean))
                .build()
                .execute(new GenericsCallback<DeleteSceneResultBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        Toast.makeText(getActivity(), "错误 code = " + code, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(DeleteSceneResultBean response, int id, int code) {
                        int responseCode = response.getCode();
                        String message = response.getMessage();
                        if (responseCode == 1000) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            mSceneList.remove(position);
                            mSceneListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.delete_scene_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setmBackViewVisible(boolean isVisible){
        mBackViewVisible = isVisible;
    }

    private void initView(View view) {
        initRefreshDataView(view);
        initSceneListView(view);

        if(mBackViewVisible){
            mImBack.setVisibility(View.VISIBLE);
        }

        if (mPopupWindow == null) {
            mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_add_scene, null);
            SupportMultipleScreensUtil.scale(mPopView);
            mAddScene = (LinearLayout) mPopView.findViewById(R.id.ll_add_scene);
            mPopupWindow = new PopupWindow(mPopView, SupportMultipleScreensUtil.getScaleValue(465),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true); // 设置是否获取焦点
        mPopupWindow.setTouchable(true); // 设置是否能否触摸
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
    }

    private void initListener() {
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopIsShow = true;
                ObjectAnimator ra = ObjectAnimator.ofFloat(mIvAddScene, "rotation", 50f, 0f);
                ra.setDuration(300);
                ra.start();
            }
        });

        mAddScene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mHasLogin){
                    showLoginDialog();
                }else {
                    Intent mIntent = new Intent();
                    mIntent.setClass(getActivity(), SceneSettingActivity.class);
                    startActivityForResult(mIntent, 0);
                    mPopupWindow.dismiss();
                }
            }
        });
    }

    private void initRefreshDataView(View view) {
        mTrlRefreshSceneView = (TwinklingRefreshLayout) view.findViewById(R.id.trl_refresh_scene);
        mTrlRefreshSceneView.setBottomView(new LoadingView(getActivity()));
        mTrlRefreshSceneView.setHeaderView(new ProgressLayout(getActivity()));
        mTrlRefreshSceneView.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mRefreshSceneStatus = 0;
                getSceneDataFromServ();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        retractSceneListRefresh();
                    }
                }, 800);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mRefreshSceneStatus = 1;
                getSceneDataFromServ();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        retractSceneListRefresh();
                    }
                }, 800);
            }
        });
    }

    private void retractSceneListRefresh() {
        if (mRefreshSceneStatus == 0) {
            mTrlRefreshSceneView.finishRefreshing();
        } else if (mRefreshSceneStatus == 1) {
            mTrlRefreshSceneView.finishLoadmore();
        }
    }

    private void initSceneListView(View view) {
        SpannableStringBuilder spanable = new SpannableStringBuilder(getString(R.string.add_scene));
        ImageSpan image = new ImageSpan(getActivity(), R.drawable.ic_small_add);
        spanable.setSpan(image, 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.scene_list_recycler_view);
        final LinearLayoutManager layoutmanager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutmanager);

        mSceneListAdapter = new SceneListAdapter(mSceneList);
        recyclerView.setAdapter(mSceneListAdapter);

        mSceneListAdapter.setClickListener(new SceneListAdapter.onClickListener() {
            @Override
            public void onItemClick(int position) {
                if(!mHasLogin){
                    showLoginDialog();
                }else {
                    if( mSceneList.get(position).getSceneModel() != null){
                        SceneModel.DeviceModel[] deviceModel = mSceneList.get(position).getSceneModel().getDevicemodel();

                        Log.i(TAG, "deviceModel.length = " + deviceModel.length);

                        if(deviceModel.length == 0){
                            ToastUtils.showToast("请设置场景");
                        }
                        for(int i = 0; i < deviceModel.length; i++){
                            if(deviceModel[i].getType().equals("1")){
                                //灯控制
                                int lightSw = deviceModel[i].getSw();
                                boolean isOn = false;
                                if(lightSw == 0){
                                    isOn = false;
                                }else {
                                    isOn = true;
                                }

                                Log.i(TAG, "isOn = " + isOn);
                                mLightControlManager.startLightControl(deviceModel[i].getMac(),isOn);
                            }else {
                                String deviceName = deviceModel[i].getName();
                                int categoryId = deviceModel[i].getCategoryid();
                                int brandId = deviceModel[i].getBrandid();

                                Toast.makeText(getActivity(), "立即执行 deviceName = " + deviceName, Toast.LENGTH_SHORT).show();
                                AircControlManager aircControlManager = new AircControlManager(getActivity(), deviceName, categoryId, brandId);
                                aircControlManager.startControl();
                            }
                        }
                    }else {
                        ToastUtils.showToast("请设置场景");
                    }
                }
            }

            @Override
            public void onExeBtnClick(int position) {
                if(!mHasLogin){
                    showLoginDialog();
                }else {
                    SceneModel sceneModel = mSceneList.get(position).getSceneModel();
                    Intent mIntent = new Intent();
                    mIntent.setClass(getActivity(), SceneSettingActivity.class);
                    mIntent.putExtra("sceneModel", sceneModel);
                    startActivityForResult(mIntent, 0);
                }
            }

            @Override
            public void onDeleteBtnClick(int position) {
                if(!mHasLogin){
                    showLoginDialog();
                }else {
                    SceneModel sceneModel = mSceneList.get(position).getSceneModel();
                    int sceneId = sceneModel.getScenemodel_id();
                    deleteSceneInServDB(sceneId, position);
                }
            }
        });
    }

    private void showLoginDialog(){
        new LoginDialog(getActivity(), R.style.login_dialog, "需要先登录账号，现在去登录？", new LoginDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if(confirm){
                    Toast.makeText(getActivity(),"点击确定", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        }).setTitle("提示").show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (intent != null) {
                mRefreshSceneStatus = 1;
                getSceneDataFromServ();
            }
        }
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
