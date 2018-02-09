package com.android.funny.net;

import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.bean.BaiduPicBean;
import com.android.funny.bean.CarDetectBean;
import com.android.funny.bean.Constants;
import com.android.funny.bean.DishDetectBean;

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
     *
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
     * <p>
     * options - options列表:
     * top_num 返回预测得分top结果数，默认为5
     *
     * @return JSONObject
     */

    public Observable<DishDetectBean> dishesRecognition(String token, String image, int top_num, float filter_threshold) {
        return mService.dishesRecognition(token, image, top_num, filter_threshold);
    }

    /**
     * 车辆识别接口
     *
     * @param token
     * @param image
     * @param top_num
     * @return 该请求用于检测一张车辆图片的具体车型。即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片的车辆品牌及型号。
     */
    public Observable<CarDetectBean> carDetect(String token, String image, int top_num) {
        return mService.carDetect(token, image, top_num);
    }

    /**
     * 植物识别
     *
     * @param token
     * @param image
     * @return 该请求用于识别一张图片，即对于输入的一张图片（可正常解码，且长宽比较合适），输出植物识别结果。
     */
    public Observable<Object> plantDetect(String token, String image) {
        return mService.plantDetect(token, image);
    }

    /**
     * 获取图片
     */
    public Observable<BaiduPicBean> getImage(String word, int pn, int rn) {
        return mService.getImageList(ApiConstants.sBaiduApi_1, ApiConstants.sBaiduTn, ApiConstants.sBaiduIe, word, pn, rn);
    }
}
