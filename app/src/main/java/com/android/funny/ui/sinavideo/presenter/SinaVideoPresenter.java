package com.android.funny.ui.sinavideo.presenter;

import com.android.funny.bean.MoveListBean;
import com.android.funny.net.RxSchedulers;
import com.android.funny.net.SinaApi;
import com.android.funny.ui.base.BasePresenter;
import com.android.funny.ui.sinavideo.contract.SinaVideoContract;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by Administrator on 2018/1/27.
 */

public class SinaVideoPresenter extends BasePresenter<SinaVideoContract.View> implements SinaVideoContract.Presenter {
    private SinaApi mSinaApi;

    @Inject
    SinaVideoPresenter(SinaApi sinaApi) {
        this.mSinaApi = sinaApi;
    }


    @Override
    public void getMoveList(String city, int start, int counter) {
        mSinaApi.getMoveList(city, start, counter)
                .compose(RxSchedulers.<MoveListBean>applySchedulers())
                .compose(mView.<MoveListBean>bindToLife())
                .subscribe(new Observer<MoveListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MoveListBean videoChannelBeans) {
                        mView.loadMoveList(videoChannelBeans);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
