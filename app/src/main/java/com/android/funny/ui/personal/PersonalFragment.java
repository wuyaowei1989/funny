package com.android.funny.ui.personal;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.funny.R;
import com.android.funny.bean.Constants;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.ui.base.BaseFragment;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.comm.util.AdError;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * desc: 个人页面
 * author: Will .
 * date: 2017/9/2 .
 */
public class PersonalFragment extends BaseFragment {

    //    @BindView(R.id.diagonalLayout)
//    DiagonalLayout diagonalLayout;
    @BindView(R.id.ivAuthor)
    ImageView mIvAuthor;
    @BindView(R.id.tvContacts)
    TextView mTvContacts;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvBlog)
    TextView mTvBlog;
    @BindView(R.id.tvGithub)
    TextView mTvGithub;
    @BindView(R.id.tvEmail)
    TextView mTvEmail;
    @BindView(R.id.tvUrl)
    TextView mTvUrl;
    @BindView(R.id.tvGithubUrl)
    TextView mTvGithubUrl;
    @BindView(R.id.tvEmailUrl)
    TextView mTvEmailUrl;

    InterstitialAD iad;

    public static PersonalFragment newInstance() {
        Bundle args = new Bundle();
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getContentLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        Typeface mtypeface = Typeface.createFromAsset(getActivity().getAssets(), "font/consolab.ttf");
        mTvContacts.setTypeface(mtypeface);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/consola.ttf");
        mTvName.setTypeface(typeface);
        mTvBlog.setTypeface(typeface);
        mTvGithub.setTypeface(typeface);
        mTvEmail.setTypeface(typeface);
        mTvGithubUrl.setTypeface(typeface);
        mTvUrl.setTypeface(typeface);
        mTvEmailUrl.setTypeface(typeface);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tvUrl, R.id.tvGithubUrl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGithubUrl:
                toWeb(getResources().getString(R.string.githubUrl));
                break;
            case R.id.tvUrl:
                showAsPopup();
                break;
        }
    }

    private void toWeb(String url){
        Uri weburl = Uri.parse(url);
        Intent web_Intent = new Intent(Intent.ACTION_VIEW, weburl);
        getActivity().startActivity(web_Intent);
    }

    private InterstitialAD getIAD() {
        if (iad == null) {
            iad = new InterstitialAD(getActivity(), Constants.T_APPID, Constants.InterteristalPosID);
        }
        return iad;
    }

    private void showAD() {
        getIAD().setADListener(new AbstractInterstitialADListener() {

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("LoadInterstitialAd Fail, error code: %d, error msg: %s",
                                error.getErrorCode(), error.getErrorMsg()));
            }

            @Override
            public void onADReceive() {
                Log.i("AD_DEMO", "onADReceive");
                iad.show();
            }
        });
        iad.loadAD();
    }

    private void showAsPopup() {
        getIAD().setADListener(new AbstractInterstitialADListener() {

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("LoadInterstitialAd Fail, error code: %d, error msg: %s",
                                error.getErrorCode(), error.getErrorMsg()));
            }

            @Override
            public void onADReceive() {
                iad.showAsPopupWindow();
            }
        });
        iad.loadAD();
    }
}
