package com.android.funny.ui.imageclassify.contract;

import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.ui.base.BaseContract;

/**
 * Created by Administrator on 2018/1/31.
 */

public interface ImageClassifyContract {
    interface View extends BaseContract.BaseView {
        void loadAccessTokenData(BaiduAccessTokenBean o);

        void loadAccessTokenDataFail(String s);

        void loaDishDetectData(Object o);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取access_token
         *
         * @param ak
         * @param sk
         */
        void getAccessToken(String ak, String sk);

        /**
         * 菜品识别
         * @param access_token
         * @param img
         * @param top_num
         * @param filter_threshold
         * 该请求用于菜品识别。即对于输入的一张图片（可正常解码，且长宽比适宜），输出图片的菜品名称、卡路里信息、置信度。
         */
        void dishDetect(String access_token, String img, int top_num, float filter_threshold);
    }
}
