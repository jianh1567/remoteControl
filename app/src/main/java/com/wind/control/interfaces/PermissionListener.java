package com.wind.control.interfaces;

import java.util.List;

/**
 * Created by luow on 2017/7/7.
 */

public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermission);

}
