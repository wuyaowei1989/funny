package com.android.funny;


import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;

import com.android.funny.component.ApplicationComponent;
import com.android.funny.component.DaggerApplicationComponent;
import com.android.funny.module.ApplicationModule;
import com.android.funny.module.HttpModule;
import com.android.funny.utils.ContextUtils;
import com.hss01248.dialog.ActivityStackManager;
import com.hss01248.dialog.StyledDialog;
import com.tencent.mta.track.StatisticsDataAPI;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * desc: .
 * author: Will .
 * date: 2017/9/2 .
 */
public class MyApp extends LitePalApplication {

    private ApplicationComponent mApplicationComponent;

    private static MyApp sMyApp;

    public static int width = 0;

    public static int height = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        sMyApp = this;
        BGASwipeBackManager.getInstance().init(this);
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();
        LitePal.initialize(this);
        StyledDialog.init(this);
        registCallback();
        StatisticsDataAPI.instance(this);
        StatService.setContext(this);
        initMTAConfig(false);
        // 注册Activity生命周期监控，自动统计时长
        StatService.registerActivityLifecycleCallbacks(this);
        width = ContextUtils.getSreenWidth(MyApp.getContext());
        height = ContextUtils.getSreenHeight(MyApp.getContext());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    private void initMTAConfig(boolean isDebugMode) {

        if (isDebugMode) {
            StatConfig.setDebugEnable(true);

        } else {
            StatConfig.setDebugEnable(false);
            StatConfig.setAutoExceptionCaught(true);
            StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
            StatConfig.setSendPeriodMinutes(10);
        }
    }

    public static MyApp getInstance() {
        return sMyApp;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    private void registCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityStackManager.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityStackManager.getInstance().removeActivity(activity);
            }
        });
    }
}
