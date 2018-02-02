package com.android.funny.component;

import android.content.Context;

import com.android.funny.MyApp;
import com.android.funny.module.ApplicationModule;
import com.android.funny.module.HttpModule;
import com.android.funny.net.BaiduAiApi;
import com.android.funny.net.BaiduApi;
import com.android.funny.net.JanDanApi;
import com.android.funny.net.NewsApi;
import com.android.funny.net.SinaApi;

import dagger.Component;

/**
 * desc: .
 * author: Will .
 * date: 2017/9/2 .
 */
@Component(modules = {ApplicationModule.class,HttpModule.class})
public interface ApplicationComponent {

    MyApp getApplication();

    NewsApi getNetEaseApi();

    JanDanApi getJanDanApi();

    SinaApi getSinaApi();

    BaiduAiApi getBaiduAiApi();

    BaiduApi getBaiduApi();

    Context getContext();

}
