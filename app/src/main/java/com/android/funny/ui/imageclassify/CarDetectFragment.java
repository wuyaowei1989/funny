package com.android.funny.ui.imageclassify;

import android.os.Bundle;
import android.view.View;

import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.bean.BaiduPicBean;
import com.android.funny.bean.DishDetectBean;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.ui.base.BaseFragment;
import com.android.funny.ui.imageclassify.contract.ImageClassifyContract;
import com.android.funny.ui.imageclassify.presenter.ImageClassifyPresenter;

/**
 * Created by 14400 on 2018/2/6.
 */

public class CarDetectFragment extends BaseFragment<ImageClassifyPresenter> implements ImageClassifyContract.View {

    @Override
    public int getContentLayout() {
        return 0;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void loadAccessTokenData(BaiduAccessTokenBean o) {

    }

    @Override
    public void loadAccessTokenDataFail(String s) {

    }

    @Override
    public void loaDishDetectData(DishDetectBean o) {

    }

    @Override
    public void loadCarDetectData(Object o) {

    }

    @Override
    public void loadPlantDetectData(Object o) {

    }

    @Override
    public void loadImageData(BaiduPicBean bean) {

    }
}
