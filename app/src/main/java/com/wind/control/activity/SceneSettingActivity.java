package com.wind.control.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.view.jameson.library.CardScaleHelper;
import com.wind.control.R;
import com.wind.control.adapter.CardAdapter;
import com.wind.control.adapter.DateSelectAdapter;
import com.wind.control.adapter.SceneActionAdapter;
import com.wind.control.base.BaseActivity;
import com.wind.control.model.SceneInfoResultBean.SceneModel;
import com.wind.control.model.SceneInfoResultBean.SceneModel.DeviceModel;
import com.wind.control.model.SceneSettingBean;
import com.wind.control.model.SceneActionInfo;
import com.wind.control.model.UpdateSceneInfo;
import com.wind.control.model.AddSceneInfo;
import com.wind.control.model.TimerDateInfo;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.Constants;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by 1003373 on 2018/8/16.
 */

public class SceneSettingActivity extends BaseActivity {
    private static final String TAG = "SceneSettingActivity";
    @BindView(R.id.tv_activity_title)
    TextView mTvActivityTitle;
    @BindView(R.id.et_scene_name)
    EditText mEtSceneName;
    private RecyclerView mRecyclerView;
    private List<Integer> mList;
    private CardScaleHelper mCardScaleHelper = null;
    private ArrayList<TimerDateInfo> mDateSelectList = new ArrayList<>();
    private RecyclerView mdateSelectRcv;
    private RelativeLayout mTimeRl;
    private TextView mTimeTv;
    private String mSelectHour;
    private String mSelectMinute;
    private ImageView mAddAction;
    private RecyclerView mActionRecylerView;
    private List<SceneActionInfo> mSceneActionInfoList = new ArrayList<>();
    private List<SceneActionInfo> mDeleteActionInfoList = new ArrayList<>();
    private SceneActionAdapter mSceneActionAdapter;
    private String mActionName = "";
    private int mSwStatus = 0;
    private String mModel = "";
    private String mTemp = "";
    private String mSpeed = "";
    private String mPostWeek = "";
    private String mPostTime = "";
    private String mPostSceneName = "";
    private String mWeek = "";
    private String[] mSplitWeek;
    private String mSceneName = "";
    private String mTime = "";
    private int mDeviceModelId;
    private DeviceModel[] mDeviceModel;
    List<UpdateSceneInfo.DeviceModel> mUpdateSceneInfos = new ArrayList<>();
    List<AddSceneInfo.SceneDeviceInfo> mAddSceneInfos = new ArrayList<>();
    private boolean mUpdateScene = false;
    private Button mUpdateSaveBtn;
    private int mDid;
    private int mSceneModelId;
    private int mUpdatePosition;
    private SceneActionInfo mSceneActionInfo;
    private String mDeviceType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_setting);
        ButterKnife.bind(this);

        setActivityTitle(mTvActivityTitle, R.string.scene_setting);

        initData();
        initSceneSettingData();
        initView();
    }

    private void initSceneSettingData(){
        Intent intent = getIntent();
        SceneModel sceneModel = (SceneModel) intent.getSerializableExtra("sceneModel");
        if(sceneModel != null){
            mUpdateScene = true;
            mWeek = sceneModel.getWeek();
            mSceneName = sceneModel.getScenename();
            mTime = sceneModel.getTime();
            mDeviceModel = sceneModel.getDevicemodel();
            mSceneModelId = sceneModel.getScenemodel_id();
            initDeviceModelData(mDeviceModel);
            mSplitWeek = mWeek.split(",");
            for(int i = 0 ; i < mSplitWeek.length; i++){
                if(!mSplitWeek[i].equals("")){
                    int week = Integer.parseInt(mSplitWeek[i]);
                    Log.i(TAG, "initSceneSettingData week = " + week);
                    mDateSelectList.get(week).setChecked(true);
                }
            }
        }else {
            mUpdateScene = false;
        }
    }

    private void initDeviceModelData(DeviceModel[] deviceModels){
        Log.i(TAG, "initDeviceModelData deviceModels.length = " + deviceModels.length);
        for(int i = 0; i < deviceModels.length; i++){
            mActionName = deviceModels[i].getName();
            mSwStatus = deviceModels[i].getSw();
            mModel = deviceModels[i].getModel();
            mTemp = deviceModels[i].getTemperature();
            mSpeed = deviceModels[i].getWindspeed();
            mDeviceModelId = deviceModels[i].getDevicemodel_id();
            mDid = deviceModels[i].getD_id();
            mDeviceType = deviceModels[i].getType();
            SceneActionInfo sceneActionInfo = new SceneActionInfo(mActionName, mSwStatus, mModel,
                    mTemp, mSpeed, mDeviceModelId, -10, mDid, mDeviceType);
            mSceneActionInfoList.add(sceneActionInfo);
        }
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(R.drawable.scene_bedroom);
        mList.add(R.drawable.scene_drawing_room);
        mList.add(R.drawable.scene_toilet);

        TimerDateInfo timerDateInfo = new TimerDateInfo("一", false);
        TimerDateInfo timerDateInfo1 = new TimerDateInfo("二", false);
        TimerDateInfo timerDateInfo2 = new TimerDateInfo("三", false);
        TimerDateInfo timerDateInfo3 = new TimerDateInfo("四", false);
        TimerDateInfo timerDateInfo4 = new TimerDateInfo("五", false);
        TimerDateInfo timerDateInfo5 = new TimerDateInfo("六", false);
        TimerDateInfo timerDateInfo6 = new TimerDateInfo("日", false);
        mDateSelectList.add(timerDateInfo);
        mDateSelectList.add(timerDateInfo1);
        mDateSelectList.add(timerDateInfo2);
        mDateSelectList.add(timerDateInfo3);
        mDateSelectList.add(timerDateInfo4);
        mDateSelectList.add(timerDateInfo5);
        mDateSelectList.add(timerDateInfo6);
    }

    private void initView() {
        initCardView();
        initEditSceneView();
        initDateSelectView();
        initTimeSelectView();
        initAddActionView();
        initAcitonRecylerView();
        initSaveUpdateBtnView();
    }

    private void initEditSceneView(){
        if(!mSceneName.isEmpty()){
            mEtSceneName.setText(mSceneName);
        }
    }

    private void initCardView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new CardAdapter(mList));
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);
    }

    private void initDateSelectView() {
        mdateSelectRcv = (RecyclerView) findViewById(R.id.rcv_date_select);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mdateSelectRcv.setLayoutManager(layoutmanager);

        final DateSelectAdapter adapter = new DateSelectAdapter(mDateSelectList);
        mdateSelectRcv.setAdapter(adapter);
        adapter.setOnItemClickListner(new DateSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mDateSelectList.get(position).getChecked()) {
                    mDateSelectList.get(position).setChecked(false);
                    Log.i(TAG, "initDateSelectView position false = " + position);
                } else {
                    mDateSelectList.get(position).setChecked(true);
                    Log.i(TAG, "initDateSelectView position true = " + position);
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initTimeSelectView() {
        int selectHour = 0;
        int selectMinute = 0;

        final Calendar calendar = Calendar.getInstance();
        mTimeRl = (RelativeLayout) findViewById(R.id.rl_scene_setting);
        mTimeTv = (TextView) findViewById(R.id.tv_scene_setting);

        if(!mTime.isEmpty()){
            selectHour = Integer.parseInt(mTime.substring(0,2));
            selectMinute = Integer.parseInt(mTime.substring(2,4));
            mTimeTv.setText(selectHour + ":" + selectMinute + "启动");
        }else {
            selectHour = calendar.get(Calendar.HOUR_OF_DAY);
            selectMinute = calendar.get(Calendar.MINUTE);
            mTimeTv.setText(selectHour + ":" + selectMinute + "启动");
        }

        final int finalSelectHour = selectHour;
        final int finalSelectMinute = selectMinute;

        mTimeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(SceneSettingActivity.this, 2, null, finalSelectHour, finalSelectMinute);
            }
        });
    }

    private void initAddActionView() {
        mAddAction = (ImageView) findViewById(R.id.iv_add_action);
        mAddAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.setClass(SceneSettingActivity.this, SceneAddActionActivity.class);
                startActivityForResult(mIntent, 0);
            }
        });
    }

    private void initAcitonRecylerView() {
        mActionRecylerView = (RecyclerView) findViewById(R.id.rcv_add_action);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mActionRecylerView.setLayoutManager(linearLayoutManager);

        mSceneActionAdapter = new SceneActionAdapter(mSceneActionInfoList);
        mSceneActionAdapter.setOnDelClickListener(new SceneActionAdapter.onClickListener() {
            @Override
            public void onDelteBtnClick(int position) {
                if(mSceneActionInfoList.get(position).getDeviceModelId() != -1){
                    mDeleteActionInfoList.add(mSceneActionInfoList.get(position));
                }

                mSceneActionInfoList.remove(position);
                mSceneActionAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onItemClick(int position) {
                mUpdatePosition = position;
                mSceneActionInfo = mSceneActionInfoList.get(position);
                if(mSceneActionInfo.getDeviceType().equals("1")){
                    Intent intent = new Intent();
                    intent.setClass(SceneSettingActivity.this, SceneLightControlActivity.class);
                    intent.putExtra("actionInfo", mSceneActionInfo);
                    startActivityForResult(intent, 1);
                }else if(mSceneActionInfo.getDeviceType().equals("2")){
                    Intent intent = new Intent();
                    intent.setClass(SceneSettingActivity.this, SceneAircControlActivity.class);
                    intent.putExtra("actionInfo", mSceneActionInfo);
                    startActivityForResult(intent, 1);
                }
            }
        });

        mActionRecylerView.setAdapter(mSceneActionAdapter);
    }

    private void initSaveUpdateBtnView(){
        mUpdateSaveBtn = (Button) findViewById(R.id.btn_save_update);
        if(mUpdateScene){
            mUpdateSaveBtn.setText(R.string.update_scene);
        }else {
            mUpdateSaveBtn.setText(R.string.save_scene);
        }
    }

    /**
     * 时间选择
     *
     * @param activity
     * @param themeResId
     * @param tv
     * @param
     */
    public void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, int hour, int minute) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        Toast.makeText(SceneSettingActivity.this, hourOfDay + "时" + minute + "分", Toast.LENGTH_SHORT).show();
                        showTimeSelect(hourOfDay, minute);
                    }

                }
                // 设置初始时间
                , hour
                , minute
                // true表示采用24小时制
                , true).show();
    }

    private void showTimeSelect(int hour, int minute) {
        if (hour < 10) {
            mSelectHour = "0" + hour;
        } else {
            mSelectHour = String.valueOf(hour);
        }

        if (minute < 10) {
            mSelectMinute = "0" + minute;
        } else {
            mSelectMinute = String.valueOf(minute);
        }

        mTimeTv.setText(mSelectHour + ":" + mSelectMinute + "启动");
        mPostTime = mSelectHour + mSelectMinute;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == Constants.ARICCODE) {
                mActionName = intent.getStringExtra("actionName");
                mSwStatus = intent.getIntExtra("aircStatus", 0);
                mDid = intent.getIntExtra("id", 0);
                mDeviceType = intent.getStringExtra("type");
                mModel = intent.getStringExtra("aircModel");
                mTemp = intent.getStringExtra("aircTemp");
                mSpeed = intent.getStringExtra("aircSpeed");
                addDataToSceneActionList();
            } else if (resultCode == Constants.LIGHTCODE) {
                mActionName = intent.getStringExtra("actionName");
                mSwStatus = intent.getIntExtra("lightStatus", 0);
                mDid = intent.getIntExtra("id", 0);
                mDeviceType = intent.getStringExtra("type");
                addDataToSceneActionList();
            }
        }else if(requestCode == 1){
            if (resultCode == Constants.ARICCODE) {
                mActionName = intent.getStringExtra("actionName");
                mSwStatus = intent.getIntExtra("aircStatus", 0);
                mDid = intent.getIntExtra("id", 0);
                mModel = intent.getStringExtra("aircModel");
                mTemp = intent.getStringExtra("aircTemp");
                mSpeed = intent.getStringExtra("aircSpeed");

                mSceneActionInfo.setStatus(mSwStatus);
                mSceneActionInfo.setModle(mModel);
                mSceneActionInfo.setTemp(mTemp);
                mSceneActionInfo.setSpeed(mSpeed);
                mSceneActionInfo.setChangetype(1);
                mSceneActionInfoList.set(mUpdatePosition, mSceneActionInfo);
                mSceneActionAdapter.notifyDataSetChanged();
            } else if (resultCode == Constants.LIGHTCODE) {
                mActionName = intent.getStringExtra("actionName");
                mSwStatus = intent.getIntExtra("lightStatus", 0);
                mDid = intent.getIntExtra("id", 0);

                mSceneActionInfo.setStatus(mSwStatus);
                mSceneActionInfo.setChangetype(1);
                mSceneActionInfoList.set(mUpdatePosition, mSceneActionInfo);
                mSceneActionAdapter.notifyDataSetChanged();
            }
        }
    }

    private void addDataToSceneActionList(){
        SceneActionInfo sceneAddActionInfo = new SceneActionInfo(mActionName, mSwStatus,
                                              mModel, mTemp, mSpeed, -1, 2, mDid, mDeviceType);
        mSceneActionInfoList.add(sceneAddActionInfo);
        mSceneActionAdapter.notifyItemInserted(mSceneActionInfoList.size());
    }

    public void onClickSaveUpdateBtn(View view) {
        setPostDataToServ();

        Log.i(TAG, "onClickSaveBtn");
        if(mEtSceneName.getText().toString().isEmpty()){
            ToastUtils.showToast(getString(R.string.inpute_name));
            return;
        }

        if(mPostWeek.isEmpty()){
            ToastUtils.showToast(getString(R.string.inpute_date));
            return;
        }

        if(mTimeTv.getText().length() == 0 ){
            ToastUtils.showToast(getString(R.string.inpute_name));
            return;
        }

        if(mSceneActionInfoList.size() == 0){
            ToastUtils.showToast(getString(R.string.please_add_action));
            return;
        }

        if(mUpdateScene){
            postUpdateSceneToServ();
        }else {
            postAddSceneToServ();
        }
    }

    private void setPostDataToServ() {
        List<Integer> dateSelects = new ArrayList<>();

        for (int i = 0; i < mDateSelectList.size(); i++) {
            if (mDateSelectList.get(i).getChecked()) {
                dateSelects.add(i);
                Log.i(TAG, "setDataToPostServ i = " + i);
            }
        }

        mPostWeek = "";
        for (int i = 0; i < dateSelects.size(); i++) {
            if (i == dateSelects.size() - 1) {
                mPostWeek = mPostWeek + String.valueOf(dateSelects.get(i));
            } else {
                mPostWeek = mPostWeek + String.valueOf(dateSelects.get(i)) + ",";
            }
        }
        Log.i(TAG, "setDataToPostServ mPostWeek = " + mPostWeek);

        if(mUpdateScene){
            for(SceneActionInfo sceneAddActionInfo: mSceneActionInfoList) {
                UpdateSceneInfo.DeviceModel sceneDeviceInfo = new UpdateSceneInfo.DeviceModel();
                sceneDeviceInfo.setChangetype(sceneAddActionInfo.getChangetype());
                sceneDeviceInfo.setD_id(sceneAddActionInfo.getD_id());
                sceneDeviceInfo.setDeviceModelId(sceneAddActionInfo.getDeviceModelId());
                sceneDeviceInfo.setSw(sceneAddActionInfo.getStatus());
                sceneDeviceInfo.setModel(sceneAddActionInfo.getModle());
                sceneDeviceInfo.setTemperature(sceneAddActionInfo.getTemp());
                sceneDeviceInfo.setWindspeed(sceneAddActionInfo.getSpeed());
                mUpdateSceneInfos.add(sceneDeviceInfo);
            }

            for(SceneActionInfo sceneActionInfo : mDeleteActionInfoList){
                UpdateSceneInfo.DeviceModel sceneDeviceInfo = new UpdateSceneInfo.DeviceModel();
                sceneDeviceInfo.setChangetype(3);
                sceneDeviceInfo.setDeviceModelId(sceneActionInfo.getDeviceModelId());
                sceneDeviceInfo.setSw(0);
                sceneDeviceInfo.setTemperature("");
                sceneDeviceInfo.setWindspeed("");
                sceneDeviceInfo.setModel("");
                sceneDeviceInfo.setD_id(sceneActionInfo.getD_id());
                mUpdateSceneInfos.add(sceneDeviceInfo);
            }
        }else {
            for(SceneActionInfo sceneAddActionInfo: mSceneActionInfoList) {
                AddSceneInfo.SceneDeviceInfo sceneDeviceInfo = new AddSceneInfo.SceneDeviceInfo();
                sceneDeviceInfo.setId(sceneAddActionInfo.getD_id());
                sceneDeviceInfo.setSw(sceneAddActionInfo.getStatus());
                sceneDeviceInfo.setModel(sceneAddActionInfo.getModle());
                sceneDeviceInfo.setTemperature(sceneAddActionInfo.getTemp());
                sceneDeviceInfo.setWindspeed(sceneAddActionInfo.getSpeed());
                mAddSceneInfos.add(sceneDeviceInfo);
            }
        }

        mPostSceneName = mEtSceneName.getText().toString();
    }

    private void postUpdateSceneToServ() {
        UpdateSceneInfo updateSceneInfo = new UpdateSceneInfo();
        updateSceneInfo.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        updateSceneInfo.setScenename(mPostSceneName);
        updateSceneInfo.setTime(mPostTime);
        updateSceneInfo.setWeek(mPostWeek);
        updateSceneInfo.setScenemodel_id(mSceneModelId);
        updateSceneInfo.setToken(BaseInfoSPUtil.getInstance().getLoginToken(this));

        Log.i(TAG, "mSceneDeviceInfos.size() = " + mUpdateSceneInfos.size());
        if(mUpdateSceneInfos.size() > 0){
            UpdateSceneInfo.DeviceModel[] sceneDeviceInfos = mUpdateSceneInfos.toArray(new UpdateSceneInfo.DeviceModel[mUpdateSceneInfos.size()]);
            updateSceneInfo.setDevicemodel(sceneDeviceInfos);
        }else {
            updateSceneInfo.setDevicemodel(null);
        }

        String json = new Gson().toJson(updateSceneInfo);
        Log.i(TAG, "json = " + json );

        OkHttpUtils.postString()
                .url(Api.UPDATE_SCENE_SETTING)
                .content(json)
                .build()
                .execute(new GenericsCallback<SceneSettingBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        ToastUtils.showToast(getString(R.string.net_error));
                    }

                    @Override
                    public void onResponse(SceneSettingBean response, int id, int code) {
                        int mCode = response.getCode();
                        String message = response.getMessage();
                        Log.i("minos", "mCode = " + mCode + " message = " + message);
                        if(mCode == 1000){
                            ToastUtils.showToast(getString(R.string.update_success));
                            Intent intent = new Intent();
                            setResult(1, intent);
                            SceneSettingActivity.this.finish();
                        }else {
                            ToastUtils.showToast(getString(R.string.update_fail));
                        }
                    }
                });
    }

    private void postAddSceneToServ() {
        AddSceneInfo addSceneInfo = new AddSceneInfo();
        addSceneInfo.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        addSceneInfo.setScenename(mPostSceneName);
        addSceneInfo.setTime(mPostTime);
        addSceneInfo.setWeek(mPostWeek);
        addSceneInfo.setToken(BaseInfoSPUtil.getInstance().getLoginToken(this));

        if(mAddSceneInfos.size() > 0){
            AddSceneInfo.SceneDeviceInfo[] sceneDeviceInfos = mAddSceneInfos.toArray(
                            new AddSceneInfo.SceneDeviceInfo[mAddSceneInfos.size()]);
            addSceneInfo.setD_id(sceneDeviceInfos);
        }

        String json = new Gson().toJson(addSceneInfo);
        Log.i(TAG, "json = " + json);
        OkHttpUtils.postString()
                .url(Api.SAVE_SCENE_SETTING)
                .content(json)
                .build()
                .execute(new GenericsCallback<SceneSettingBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                    }

                    @Override
                    public void onResponse(SceneSettingBean response, int id, int code) {
                        int mCode = response.getCode();
                        String message = response.getMessage();
                        Log.i("minos", "mCode = " + mCode + " message = " + message);
                        if(mCode == 1000){
                            ToastUtils.showToast(getString(R.string.add_success));
                            Intent intent = new Intent();
                            setResult(1, intent);
                            SceneSettingActivity.this.finish();
                        }else {
                            ToastUtils.showToast(getString(R.string.add_fail));
                        }
                    }
                });
    }

}
