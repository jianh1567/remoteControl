package com.wind.control.activity.remote;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.wind.control.R;
import com.wind.control.activity.EsptouchDemoActivity;
import com.wind.control.base.BaseActivity;
import com.wind.control.interfaces.PermissionListener;

import java.util.List;

import butterknife.OnClick;

public class SelecteTypeActivity extends BaseActivity {
    private static final int CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }
    }

    @OnClick({R.id.ll_device_stb, R.id.ll_device_ac, R.id.ll_device_box,
            R.id.ll_device_dvd, R.id.ll_device_pa, R.id.ll_device_tv,
            R.id.ll_device_projector, R.id.ll_device_fan, R.id.ll_device_light,
            R.id.ll_device_purifier, R.id.ll_wifi, R.id.iv_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.ll_device_stb:
                //机顶盒
                Intent itStb = new Intent(this, BrandActivity.class);
                itStb.putExtra("id", 11);
                startActivity(itStb);
                break;
            case R.id.ll_device_ac:
                //空调
                Intent it = new Intent(this, BrandActivity.class);
                it.putExtra("id", 1);
                startActivity(it);
                break;
            case R.id.ll_device_box:
                //盒子
                Intent itBox = new Intent(this, BrandActivity.class);
                itBox.putExtra("id", 4);
                startActivity(itBox);

                break;
            case R.id.ll_device_dvd:
                //Dvd
                Intent itDvD = new Intent(this, BrandActivity.class);
                itDvD.putExtra("id", 6);
                startActivity(itDvD);
                break;
            case R.id.ll_device_pa:
                //音响
                Intent itPa = new Intent(this, BrandActivity.class);
                itPa.putExtra("id", 9);
                startActivity(itPa);
                break;
            case R.id.ll_device_tv:
                Intent itTv = new Intent(this, BrandActivity.class);
                itTv.putExtra("id", 2);
                startActivity(itTv);
                break;
            case R.id.ll_device_projector:
                //投影仪
//                Intent itProjector = new Intent(this, BrandActivity.class);
//                itProjector.putExtra("id", 2);
//                startActivity(itProjector);
                break;
            case R.id.ll_device_fan:
                //风扇
                Intent itFan = new Intent(this, BrandActivity.class);
                itFan.putExtra("id", 7);
                startActivity(itFan);
                break;
            case R.id.ll_device_light:
                //灯
                Intent itLight = new Intent(this, BrandActivity.class);
                itLight.putExtra("id", 10);
                startActivity(itLight);
                break;
            case R.id.ll_device_purifier:
                //空气净化器
                Intent itPurifier = new Intent(this, BrandActivity.class);
                itPurifier.putExtra("id", 13);
                startActivity(itPurifier);
                break;
            case R.id.ll_wifi:
                //wifi配网
                Intent wifi = new Intent(this, EsptouchDemoActivity.class);
                startActivity(wifi);
                break;
        }
    }

    private void checkPermission() {

        requestRuntimePermission(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        }, new PermissionListener() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                Toast.makeText(SelecteTypeActivity.this, "请开启存储权限", Toast.LENGTH_SHORT).show();
                startAppSettings();
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
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE) {
            checkPermission();
        }
    }
}
