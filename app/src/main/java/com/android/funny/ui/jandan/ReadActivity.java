package com.android.funny.ui.jandan;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.funny.R;
import com.android.funny.bean.Constants;
import com.android.funny.bean.FreshNewsArticleBean;
import com.android.funny.bean.FreshNewsBean;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.net.BaseObserver;
import com.android.funny.net.JanDanApi;
import com.android.funny.net.RxSchedulers;
import com.android.funny.ui.base.BaseActivity;
import com.android.funny.utils.DateUtil;
import com.android.funny.utils.StatusBarUtil;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ReadActivity extends BaseActivity {
    private static final String DATA = "data";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_author)
    TextView mTvAuthor;
    @BindView(R.id.tv_excerpt)
    TextView mTvExcerpt;
    @BindView(R.id.wv_contnet)
    WebView mWebView;
    @BindView(R.id.progress_wheel)
    ProgressBar progressWheel;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    JanDanApi mJanDanApi;
    FreshNewsBean.PostsBean postsBean;
    FreshNewsArticleBean newsArticleBean;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.iv_comment)
    ImageView mIvComment;
    @BindView(R.id.container)
    FrameLayout mContainer;

    private ViewGroup container;
    private NativeExpressAD nativeExpressAD;
    private NativeExpressADView nativeExpressADView;
    private NativeExpressAD.NativeExpressADListener mNativeExpressADListener;
    InterstitialAD iad;

    public static void launch(Context context, FreshNewsBean.PostsBean postsBean, View view) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(DATA, postsBean);
        context.startActivity(intent);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_read;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        mJanDanApi = appComponent.getJanDanApi();
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageView(this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA, getStateView());
        if (getIntent().getExtras() == null) return;
        postsBean = (FreshNewsBean.PostsBean) getIntent().getSerializableExtra(DATA);
        if (postsBean == null) return;

        mTvTitle.setText(postsBean.getTitle());
        mTvAuthor.setText(postsBean.getAuthor().getName()
                + "  "
                + DateUtil.getTimestampString(DateUtil.string2Date(postsBean.getDate(), "yyyy-MM-dd HH:mm:ss")));
        mTvExcerpt.setText(postsBean.getExcerpt());
        showSuccess();
        setWebViewSetting();

        container = (ViewGroup)mContainer;
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

    private void refreshAd() {
        /**
         *  如果选择支持视频的模版样式，请使用{@link Constants#NativeExpressSupportVideoPosID}
         */
        nativeExpressAD = new NativeExpressAD(this, getMyADSize(), Constants.T_APPID, Constants.NativeExpressPosID, mNativeExpressADListener); // 这里的Context必须为Activity
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

    private void AdInterval() {
        Observable.interval(30000, 30000, TimeUnit.MILLISECONDS)
                //延时3000 ，每间隔3000，时间单位
                .compose(this.<Long>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    refreshAd();
                });
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
            iad = new InterstitialAD(this, Constants.T_APPID, Constants.DetailNativePosID);
        }
        return iad;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onRetry() {
        getData(postsBean.getId());
    }

    private void setWebViewSetting() {
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setVerticalScrollbarOverlay(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setHorizontalScrollbarOverlay(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.loadUrl("file:///android_asset/jd/post_detail.html");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getData(postsBean.getId());
            }
        });
    }

    private void getData(int id) {
        mJanDanApi.getFreshNewsArticle(id)
                .compose(RxSchedulers.<FreshNewsArticleBean>applySchedulers())
                .compose(this.<FreshNewsArticleBean>bindToLifecycle())
                .subscribe(new BaseObserver<FreshNewsArticleBean>() {
                    @Override
                    public void onSuccess(final FreshNewsArticleBean articleBean) {
                        if (articleBean == null) {
                            showFaild();
                        } else {
                            newsArticleBean = articleBean;
                            mWebView.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressWheel.setVisibility(View.GONE);
                                    final String content = articleBean.getPost().getContent();
                                    String url = "javascript:show_content(\'" + content + "\')";
                                    mWebView.loadUrl(url);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        showFaild();
                    }
                });
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }


    @OnClick({R.id.iv_back, R.id.iv_share, R.id.iv_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_comment:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
