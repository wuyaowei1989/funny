package com.android.funny.ui.sinavideo.presenter;

import com.android.funny.net.SinaApi;
import com.android.funny.ui.base.BasePresenter;
import com.android.funny.ui.sinavideo.contract.SinaVideoContract;

import javax.inject.Inject;


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
    }
}
