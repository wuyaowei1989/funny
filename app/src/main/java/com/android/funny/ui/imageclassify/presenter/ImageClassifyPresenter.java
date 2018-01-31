package com.android.funny.ui.imageclassify.presenter;

import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.net.BaiduAiApi;
import com.android.funny.net.BaseObserver;
import com.android.funny.net.RxSchedulers;
import com.android.funny.ui.base.BasePresenter;
import com.android.funny.ui.imageclassify.contract.ImageClassifyContract;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/31.
 */

public class ImageClassifyPresenter extends BasePresenter<ImageClassifyContract.View> implements ImageClassifyContract.Presenter {

    BaiduAiApi mBaiduAiApi;

    @Inject
    public ImageClassifyPresenter(BaiduAiApi baiduAiApi) {
        this.mBaiduAiApi = baiduAiApi;
    }

    @Override
    public void getAccessToken(String ak, String sk) {
        mBaiduAiApi.getAccessToken(ak, sk)
                .compose(RxSchedulers.<BaiduAccessTokenBean>applySchedulers())
                .compose(mView.<BaiduAccessTokenBean>bindToLife())
                .subscribe(new BaseObserver<BaiduAccessTokenBean>() {
                    @Override
                    public void onSuccess(BaiduAccessTokenBean itemBeen) {

                        mView.loadAccessTokenData(itemBeen);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        mView.loadAccessTokenDataFail(e.getMessage());
                    }
                });
    }
}
