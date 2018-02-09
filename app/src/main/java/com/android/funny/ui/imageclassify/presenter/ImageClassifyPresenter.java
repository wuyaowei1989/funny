package com.android.funny.ui.imageclassify.presenter;

import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.bean.BaiduPicBean;
import com.android.funny.bean.CarDetectBean;
import com.android.funny.bean.DishDetectBean;
import com.android.funny.net.BaiduAiApi;
import com.android.funny.net.BaseObserver;
import com.android.funny.net.RxSchedulers;
import com.android.funny.ui.base.BasePresenter;
import com.android.funny.ui.imageclassify.contract.ImageClassifyContract;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

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

    @Override
    public void dishDetect(String access_token, String img, int top_num, float filter_threshold) {
        mBaiduAiApi.dishesRecognition(access_token, img, top_num, filter_threshold)
                .compose(RxSchedulers.<DishDetectBean>applySchedulers())
                .compose(mView.<DishDetectBean>bindToLife())
                .subscribe(new BaseObserver<DishDetectBean>() {
                    @Override
                    public void onSuccess(DishDetectBean itemBeen) {

                        mView.loaDishDetectData(itemBeen);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        //
                    }
                });
    }

    @Override
    public void carDetect(String access_token, String img, int top_num) {
        mBaiduAiApi.carDetect(access_token, img, top_num)
                .compose(RxSchedulers.<CarDetectBean>applySchedulers())
                .compose(mView.<CarDetectBean>bindToLife())
                .map(new Function<CarDetectBean, CarDetectBean>() {
                    @Override
                    public CarDetectBean apply(@NonNull CarDetectBean detailBean) throws Exception {
                        for (CarDetectBean.ResultBean bean : detailBean.getResult()) {
                            bean.color = detailBean.getColor_result();
                        }
                        return detailBean;
                    }
                })
                .subscribe(new BaseObserver<CarDetectBean>() {
                    @Override
                    public void onSuccess(CarDetectBean itemBeen) {

                        mView.loadCarDetectData(itemBeen);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        //
                    }
                });
    }

    @Override
    public void plantDetect(String token, String image) {
        mBaiduAiApi.plantDetect(token, image)
                .compose(RxSchedulers.<Object>applySchedulers())
                .compose(mView.<Object>bindToLife())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    public void onSuccess(Object itemBeen) {

                        mView.loadPlantDetectData(itemBeen);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        //
                    }
                });
    }

    @Override
    public void getImageList(String word, int pageNum, int pageSize) {
        int pn = pageNum == 0 ? pageNum : (pageNum * pageSize + 1);
        mBaiduAiApi.getImage(word, pn, pageSize)
                .compose(RxSchedulers.<BaiduPicBean>applySchedulers())
                .compose(mView.<BaiduPicBean>bindToLife())
                .subscribe(new BaseObserver<BaiduPicBean>() {
                    @Override
                    public void onSuccess(BaiduPicBean itemBeen) {
                        mView.loadImageData(itemBeen);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        //
                    }
                });
    }
}
