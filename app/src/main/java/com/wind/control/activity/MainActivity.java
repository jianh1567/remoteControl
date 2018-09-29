package com.wind.control.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.wind.control.R;
import com.wind.control.base.BaseActivity;
import com.wind.control.fragment.DeviceFragment;
import com.wind.control.fragment.HomeFragment;
import com.wind.control.fragment.MineFragment;
import com.wind.control.fragment.SceneFragment;
import com.wind.control.widget.BottomTabItem;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by W010003373 on 2018/8/7.
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.fragmentContent)
    FrameLayout mFlLayout;
    @BindView(R.id.tab_home_page)
    BottomTabItem mTabHomePage;
    @BindView(R.id.tab_device)
    BottomTabItem mTabDevice;
    @BindView(R.id.tab_scene)
    BottomTabItem mTabScene;
    @BindView(R.id.tab_mine)
    BottomTabItem mTabMine;
    //我的家
    private HomeFragment mHomeFragment;

    //场景
    private SceneFragment mSceneFragment;

    //设备
    private DeviceFragment mDeviceFragment;

    //我的
    private MineFragment mMineFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTab(1);
    }

    @OnClick({R.id.tab_home_page, R.id.tab_scene, R.id.tab_device, R.id.tab_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_home_page:
                setTab(1);
                break;
            case R.id.tab_scene:
                setTab(2);
                break;
            case R.id.tab_device:
                setTab(3);
                break;
            case R.id.tab_mine:
                setTab(4);
                break;
        }
    }

    public void setTab(int index) {
        switch (index) {
            case 1:
                setOtherTabsNormal();
                showHomeFragment();
                break;
            case 2:
                setOtherTabsNormal();
                showSceneFragment();
                break;
            case 3:
                setOtherTabsNormal();
                showDeviceFragment();
                break;
            case 4:
                setOtherTabsNormal();
                showMineFragment();
                break;
            default:
                break;
        }
    }

    /**
     * 我的家
     */
    public void showHomeFragment() {
        mTabHomePage.setTabSelected(true);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.fragmentContent, mHomeFragment);
        }
        commitShowFragment(fragmentTransaction, mHomeFragment);
    }

    /**
     * 发现
     */
    public void showSceneFragment() {
        mTabScene.setTabSelected(true);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        hideAllFragment(fragmentTransaction);
        if (mSceneFragment == null) {
            mSceneFragment = SceneFragment.getInstance();
            fragmentTransaction.add(R.id.fragmentContent, mSceneFragment);
        }

//        mSceneFragment.getSceneDataFromServ();
        commitShowFragment(fragmentTransaction, mSceneFragment);
    }

    /**
     * 设备
     */
    public void showDeviceFragment() {
        mTabDevice.setTabSelected(true);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        hideAllFragment(fragmentTransaction);
        if (mDeviceFragment == null) {
            mDeviceFragment = new DeviceFragment();
            fragmentTransaction.add(R.id.fragmentContent, mDeviceFragment);
        }
        commitShowFragment(fragmentTransaction, mDeviceFragment);

    }

    /**
     * 我的
     */
    public void showMineFragment() {
        mTabMine.setTabSelected(true);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        hideAllFragment(fragmentTransaction);
        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
            fragmentTransaction.add(R.id.fragmentContent, mMineFragment);
        }
        commitShowFragment(fragmentTransaction, mMineFragment);

    }

    /**
     * 提交事务
     *
     * @param fragmentTransaction
     * @param fragment
     */
    public void commitShowFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction
     */
    public void hideAllFragment(FragmentTransaction fragmentTransaction) {
        hideFragment(fragmentTransaction, mHomeFragment);
        hideFragment(fragmentTransaction, mSceneFragment);
        hideFragment(fragmentTransaction, mDeviceFragment);
        hideFragment(fragmentTransaction, mMineFragment);
    }

    /**
     * 隐藏Fragment
     */
    private void hideFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if (fragment != null) {
            fragmentTransaction.hide(fragment);
        }
    }

    /**
     * tab置为false
     */
    private void setOtherTabsNormal() {
        mTabHomePage.setTabSelected(false);
        mTabScene.setTabSelected(false);
        mTabDevice.setTabSelected(false);
        mTabMine.setTabSelected(false);
    }

}
