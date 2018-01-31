package com.android.funny.ui.imageclassify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.funny.R;
import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.bean.Constants;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.component.DaggerHttpComponent;
import com.android.funny.ui.base.BaseFragment;
import com.android.funny.ui.imageclassify.contract.ImageClassifyContract;
import com.android.funny.ui.imageclassify.presenter.ImageClassifyPresenter;
import com.baidu.aip.imageclassify.AipImageClassify;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/31.
 */

public class ImageClassifyFragment extends BaseFragment<ImageClassifyPresenter> implements ImageClassifyContract.View {


    @BindView(R.id.image_iv)
    ImageView imageIv;
    @BindView(R.id.image_layout)
    RelativeLayout imageLayout;
    @BindView(R.id.upload_img_tv)
    TextView uploadImgTv;
    @BindView(R.id.image_tv)
    TextView imageTv;
    Unbinder unbinder;

    public static ImageClassifyFragment newInstance() {
        Bundle args = new Bundle();
        ImageClassifyFragment fragment = new ImageClassifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_image_classify;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        AipImageClassify client = new AipImageClassify(Constants.BAIDU_AI_APPID, Constants.BAIDU_AI_AK, Constants.BAIDU_AI_SK);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        HashMap<String, String> options = new HashMap<String, String>();
        options.put("top_num", "3");
        String image = "src/test.jpg";
        JSONObject res = client.dishDetect(image, options);
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.image_layout, R.id.upload_img_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_layout:
                break;
            case R.id.upload_img_tv:
                break;
        }
    }

    @Override
    public void loadAccessTokenData(BaiduAccessTokenBean bean) {

    }

    @Override
    public void loadAccessTokenDataFail(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loaDishDetectData(Object o) {

    }
}
