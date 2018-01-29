package com.android.funny.net;

import com.android.funny.bean.Constants;
import com.android.funny.bean.MoveListBean;

import io.reactivex.Observable;

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
     * 获取视频频道列表
     *
     * @return
     */
    public Observable<MoveListBean> getMoveList(String city, int start, int counter){
        return mService.getMoveList(Constants.DOUBAN_ID, city, start, counter, "", "");
    }
}
