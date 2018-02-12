package com.android.funny.net;

import com.android.funny.bean.MoveListBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/1/27.
 */

public class SinaApi {

    public static SinaApi sInstance;

    private SinaApiService mService;

    public SinaApi(SinaApiService sinaApiService) {
        this.mService = sinaApiService;
    }

    public static SinaApi getInstance(SinaApiService sinaApiService) {
        if (sInstance == null)
            sInstance = new SinaApi(sinaApiService);
        return sInstance;
    }

    /**
     * 脸融合
     *
     * @return
     */
    public Observable<MoveListBean> faceMerge(String appSign, RequestBody body){
        return mService.faceMerge(appSign, body);
    }
}
