package com.android.funny.ui.jandan;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.funny.R;
import com.android.funny.bean.Constants;
import com.android.funny.bean.FreshNewsBean;
import com.android.funny.bean.JdDetailBean;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.component.DaggerHttpComponent;
import com.android.funny.ui.adapter.BoredPicAdapter;
import com.android.funny.ui.base.BaseFragment;
import com.android.funny.widget.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * desc: .
 * author: Will .
 * date: 2017/9/27 .
 */
@SuppressLint("ValidFragment")
public class JdDetailFragment extends BaseFragment<JanDanPresenter> implements JanDanContract.View {
    public static final String TYPE = "type";

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrFrameLayout mPtrFrameLayout;

    private BaseQuickAdapter mAdapter;
    private int pageNum = 1;
    private String type;
    private View view_Focus;
    private ViewGroup container;
    private NativeExpressAD nativeExpressAD;
    private NativeExpressADView nativeExpressADView;
    private NativeExpressAD.NativeExpressADListener mNativeExpressADListener;
    InterstitialAD iad;

    public JdDetailFragment(BaseQuickAdapter adapter) {
        this.mAdapter = adapter;
    }

    public static JdDetailFragment newInstance(String type, BaseQuickAdapter baseQuickAdapter) {
        Bundle args = new Bundle();
        args.putCharSequence(TYPE, type);
        JdDetailFragment fragment = new JdDetailFragment(baseQuickAdapter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_jd_detail;
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
        if (mAdapter instanceof BoredPicAdapter) {
            view_Focus = getView().inflate(getActivity(), R.layout.ad_header_view, null);
            container = (ViewGroup) view_Focus.findViewById(R.id.container);
            mAdapter.addHeaderView(view_Focus);
            mNativeExpressADListener = new NativeExpressAD.NativeExpressADListener() {
                @Override
                public void onNoAD(AdError adError) {
                    Log.i(
                            "AD_demo",
                            String.format("onNoAD, error code: %d, error msg: %s", adError.getErrorCode(),
                                    adError.getErrorMsg()));
                }

                @Override
                public void onADLoaded(List<NativeExpressADView> list) {
                    // 释放前一个展示的NativeExpressADView的资源
                    if (nativeExpressADView != null) {
                        nativeExpressADView.destroy();
                    }

                    if (container.getVisibility() != View.VISIBLE) {
                        container.setVisibility(View.VISIBLE);
                    }

                    if (container.getChildCount() > 0) {
                        container.removeAllViews();
                    }

                    nativeExpressADView = list.get(0);
                    // 广告可见才会产生曝光，否则将无法产生收益。
                    container.addView(nativeExpressADView);
                    nativeExpressADView.render();
                }

                @Override
                public void onRenderFail(NativeExpressADView nativeExpressADView) {
                    Log.i("AD_demo", "onRenderFail");
                }

                @Override
                public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                    Log.i("AD_demo", "onRenderSuccess");
                }

                @Override
                public void onADExposure(NativeExpressADView nativeExpressADView) {
                    Log.i("AD_demo", "onADExposure");
                }

                @Override
                public void onADClicked(NativeExpressADView nativeExpressADView) {
                    Log.i("AD_demo", "onADClicked");
                }

                @Override
                public void onADClosed(NativeExpressADView nativeExpressADView) {
                    if (container != null && container.getChildCount() > 0) {
                        container.removeAllViews();
                        container.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

                }

                @Override
                public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

                }
            };
            refreshAd();
            AdInterval();
        }

        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mAdapter instanceof BoredPicAdapter) {
                    refreshAd();
                }
                pageNum = 1;
                mPresenter.getData(type, pageNum);

            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setPreLoadNumber(1);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getData(type, pageNum);
                showAD();
            }
        }, mRecyclerView);

    }

    private void AdInterval() {
        Observable.interval(50000, 5000, TimeUnit.MILLISECONDS)
                //延时3000 ，每间隔3000，时间单位
                .compose(this.<Long>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    refreshAd();
                });
    }

    @Override
    public void initData() {
        if (getArguments() == null) return;
        type = getArguments().getString(TYPE);
        mPresenter.getData(type, pageNum);
    }

    @Override
    public void onRetry() {
        initData();
    }

    @Override
    public void loadFreshNews(FreshNewsBean freshNewsBean) {
        if (freshNewsBean == null) {
            mPtrFrameLayout.refreshComplete();
            showFaild();
        } else {
            pageNum++;
            mAdapter.setNewData(freshNewsBean.getPosts());
            mPtrFrameLayout.refreshComplete();
            showSuccess();
        }
    }

    @Override
    public void loadDetailData(String type, JdDetailBean jdDetailBean) {
        if (jdDetailBean == null) {
            mPtrFrameLayout.refreshComplete();
            showFaild();
        } else {
            pageNum++;
            mAdapter.setNewData(jdDetailBean.getComments());
            mPtrFrameLayout.refreshComplete();
            showSuccess();
        }
    }

    @Override
    public void loadMoreFreshNews(FreshNewsBean freshNewsBean) {
        if (freshNewsBean == null) {
            mAdapter.loadMoreFail();
        } else {
            pageNum++;
            mAdapter.addData(freshNewsBean.getPosts());
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void loadMoreDetailData(String type, JdDetailBean jdDetailBean) {
        if (jdDetailBean == null) {
            mAdapter.loadMoreFail();
        } else {
            pageNum++;
            mAdapter.addData(jdDetailBean.getComments());
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 使用完了每一个NativeExpressADView之后都要释放掉资源
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
    }

    private void refreshAd() {
        /**
         *  如果选择支持视频的模版样式，请使用{@link Constants#NativeExpressSupportVideoPosID}
         */
        nativeExpressAD = new NativeExpressAD(getContext(), getMyADSize(), Constants.T_APPID, Constants.NativeExpressPosID, mNativeExpressADListener); // 这里的Context必须为Activity
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // 设置什么网络环境下可以自动播放视频
                .setAutoPlayMuted(true) // 设置自动播放视频时，是否静音
                .build()); // setVideoOption是可选的，开发者可根据需要选择是否配置
        nativeExpressAD.loadAD(1);
    }

    private ADSize getMyADSize() {
        int w = ADSize.FULL_WIDTH;
        int h = ADSize.AUTO_HEIGHT;
        return new ADSize(w, h);
    }

    /**
     * 注意：带有视频的广告被点击后会进入全屏播放视频，此时视频可以跟随屏幕方向的旋转而旋转，
     * 请开发者注意处理好自己的Activity的运行时变更，不要让Activity销毁。
     * 例如，在AndroidManifest文件中给Activity添加属性android:configChanges="keyboard|keyboardHidden|orientation|screenSize"，
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

    private InterstitialAD getIAD() {
        if (iad == null) {
            iad = new InterstitialAD(getActivity(), Constants.T_APPID, Constants.InterteristalPosID);
        }
        return iad;
    }
}
