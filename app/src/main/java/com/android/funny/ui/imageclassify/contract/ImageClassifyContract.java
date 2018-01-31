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
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        /**
         * 获取access_token
         *
         * @param ak
         * @param sk
         */
        void getAccessToken(String ak, String sk);
    }
}
