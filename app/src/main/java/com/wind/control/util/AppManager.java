package com.wind.control.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author luow
 */
public class AppManager {
    private static List<Activity> activityStack;//活动栈
    private static AppManager mInstance;//单例

    private AppManager() {
        activityStack = new CopyOnWriteArrayList<>();
    }


    /**
     * 单例，包含synchronized
     *
     * @return instance
     */
    public static AppManager getInstance() {
        if (null == mInstance) {
            synchronized (AppManager.class) {
                if (null == mInstance) {
                    mInstance = new AppManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 栈中是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return activityStack.isEmpty();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
//    public Activity currentActivity() {
//        if (activityStack.isEmpty()) {
//            return null;
//        }
//        Activity activity = activityStack.lastElement();
//        return activity;
//    }

    /**
     * 获取前一个activity，便于返回
     *
     * @return
     */
    public Activity lastActivity() {
        if (activityStack.size() < 2) {
            return null;
        }
        return activityStack.get(activityStack.size() - 1);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
//    public void finishActivity() {
//        if (activityStack.empty()) {
//            return;
//        }
//        Activity activity = activityStack.lastElement();
//        finishActivity(activity);
//    }

    /**
     * 从栈中移除指定的Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定的Activity，移除此栈中指定activity的第一个匹配项，这里一般都是用来结束当前Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            removeActivity(activity);
        }
    }

    /**
     * 结束指定类名的Activity（Activity.this或Activity.class）
     */
    public void finishActivity(Class<? extends Activity> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    /**
     * 结束所有Activity到栈顶
     */
    public void finishActivityExcludeTop() {
        int stackSize = activityStack.size();
        if (stackSize >= 2) {
            for (int i = stackSize - 2; i >= 0; i--) {
                activityStack.get(i).finish();
                activityStack.remove(i);
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(Process.myPid());
        } catch (Exception e) {
        }
    }

    public static Activity getTopActivity() {
        if (activityStack.isEmpty()) {
            return null;
        } else {
            return activityStack.get(activityStack.size() - 1);
        }
    }

    /**
     * 退出应用程序并停止服务
     */
    public void AppExit(Context context, Intent intent) {
        try {
            context.stopService(intent);
            finishAllActivity();
        } catch (Exception e) {
        }
    }
}
