package com.android.funny.net;

import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.bean.Constants;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/1/31.
 */

public class BaiduAiApi {

    public static BaiduAiApi sInstance;

    private BaiduAiApiService mService;

    public BaiduAiApi(BaiduAiApiService baiduAiApiService) {
        this.mService = baiduAiApiService;
    }

    public static BaiduAiApi getInstance(BaiduAiApiService baiduAiApiService) {
        if (sInstance == null)
            sInstance = new BaiduAiApi(baiduAiApiService);
        return sInstance;
    }

    /**
     * 获取access_token
     * @param ak
     * @param sk
     * @return
     */
    public Observable<BaiduAccessTokenBean> getAccessToken(String ak, String sk) {
        return mService.getAccessToken(Constants.BAIDU_GRANT_TYPE, ak, sk);
    }

    /**
     * 菜品识别接口
     * 该请求用于菜品识别。即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片的菜品名称、卡路里信息、置信度。
     *
     * options - options列表:
     *   top_num 返回预测得分top结果数，默认为5
     * @return JSONObject
     */

    public Observable<Object> dishesRecognition(String token) {
        return mService.dishesRecognition(token);
    }
}
