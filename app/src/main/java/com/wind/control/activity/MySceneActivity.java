package com.wind.control.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.wind.control.R;
import com.wind.control.base.BaseActivity;
import com.wind.control.fragment.SceneFragment;

/**
 * Created by 1003373 on 2018/9/4.
 */

public class MySceneActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scene);

        SceneFragment sceneFragment = SceneFragment.getInstance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_my_scene, sceneFragment);
        fragmentTransaction.commit();
        sceneFragment.setmBackViewVisible(true);
    }
}
