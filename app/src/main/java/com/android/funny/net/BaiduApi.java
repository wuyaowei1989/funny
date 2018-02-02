package com.android.funny.net;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/2/2.
 */

public class BaiduApi {
    public static BaiduApi sInstance;

    private BaiduApiService mService;

    public BaiduApi(BaiduApiService baiduApiService) {
        this.mService = baiduApiService;
    }

    public static BaiduApi getInstance(BaiduApiService baiduApiService) {
        if (sInstance == null)
            sInstance = new BaiduApi(baiduApiService);
        return sInstance;
    }


    public Observable<Object> getImageList(String word, int pn, int rn) {
        return mService.getImageList(ApiConstants.sBaiduTn, ApiConstants.sBaiduIe, word, pn, rn);
    }
}
