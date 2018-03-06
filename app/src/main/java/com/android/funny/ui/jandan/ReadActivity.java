package com.android.funny.ui.jandan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.funny.R;
import com.android.funny.bean.FreshNewsArticleBean;
import com.android.funny.bean.FreshNewsBean;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.net.BaseObserver;
import com.android.funny.net.JanDanApi;
import com.android.funny.net.RxSchedulers;
import com.android.funny.ui.base.BaseActivity;
import com.android.funny.utils.DateUtil;
import com.android.funny.utils.ImageLoaderUtil;
import com.android.funny.utils.StatusBarUtil;

import abc.abc.abc.nm.cm.ErrorCode;
import abc.abc.abc.nm.vdo.VideoAdListener;
import abc.abc.abc.nm.vdo.VideoAdManager;
import abc.abc.abc.nm.vdo.VideoAdSettings;
import abc.abc.abc.nm.vdo.VideoInfoViewBuilder;
import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.rl_native_video_ad)
    RelativeLayout mNativeVideoAdLayout;
    @BindView(R.id.rl_video_info)
    RelativeLayout mVideoInfoLayout;

    public static void launch(Context context, FreshNewsBean.PostsBean postsBean, View view) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(DATA, postsBean);
        context.startActivity(intent);
//        context.startActivity(intent,
//                ActivityOptions.makeSceneTransitionAnimation((Activity) context, view, "topview").toBundle());
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
        setupNativeVideoAd();
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
        //原生控件点击后退关闭
        if (mNativeVideoAdLayout != null && mNativeVideoAdLayout.getVisibility() != View.GONE) {
            mNativeVideoAdLayout.removeAllViews();
            mNativeVideoAdLayout.setVisibility(View.GONE);

            //隐藏视频信息流视图
            hideVideoInfoLayout();
        }
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

    /**
     * 设置原生视频广告
     */
    private void setupNativeVideoAd() {
        // 设置视频广告
        final VideoAdSettings videoAdSettings = new VideoAdSettings();
        //		// 只需要调用一次，由于在主页窗口中已经调用了一次，所以此处无需调用
        //		VideoAdManager.getInstance().requestVideoAd(mContext);

        // 设置信息流视图，将图标，标题，描述，下载按钮对应的ID传入
        final VideoInfoViewBuilder videoInfoViewBuilder = VideoAdManager.getInstance(getApplicationContext())
                .getVideoInfoViewBuilder(
                        getApplicationContext()).setRootContainer(
                        mVideoInfoLayout)
                .bindAppIconView(R.id
                        .info_iv_icon)
                .bindAppNameView(R.id
                        .info_tv_title)
                .bindAppDescriptionView(R.id
                        .info_tv_description)
                .bindDownloadButton(R.id
                        .info_btn_download);
        // 注意：请在UI线程调用该方法
        // 获取原生视频控件
        View nativeVideoAdView = VideoAdManager.getInstance(getApplicationContext())
                .getNativeVideoAdView(getApplicationContext(),
                        videoAdSettings,
                        new VideoAdListener() {
                            @Override
                            public void onPlayStarted() {
                                Log.i("video_ad", "开始播放视频");
                                // 展示视频信息流视图
                                showVideoInfoLayout();
                            }

                            @Override
                            public void onPlayInterrupted() {
                                Toast.makeText(getApplicationContext(), "播放视频被中断", Toast.LENGTH_SHORT).show();
                                // 隐藏视频信息流视图
                                hideVideoInfoLayout();
                                // 释放资源
                                if (videoInfoViewBuilder != null) {
                                    videoInfoViewBuilder.release();
                                }
                                // 移除原生视频控件
                                if (mNativeVideoAdLayout != null) {
                                    mNativeVideoAdLayout.removeAllViews();
                                    mNativeVideoAdLayout.setVisibility(View
                                            .GONE);
                                }
                            }

                            @Override
                            public void onPlayFailed(int errorCode) {
                                Log.i("video_ad","视频播放失败");
                                switch (errorCode) {
                                    case ErrorCode.NON_NETWORK:
                                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ErrorCode.NON_AD:
                                        Toast.makeText(getApplicationContext(), "原生视频暂无广告", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ErrorCode.RESOURCE_NOT_READY:
                                        Toast.makeText(getApplicationContext(),"原生视频资源还没准备好", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ErrorCode.SHOW_INTERVAL_LIMITED:
                                        Toast.makeText(getApplicationContext(),"请勿频繁展示", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ErrorCode
                                            .WIDGET_NOT_IN_VISIBILITY_STATE:
                                        Toast.makeText(getApplicationContext(),"原生视频控件处在不可见状态", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(),"请稍后再试", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }

                            @Override
                            public void onPlayCompleted() {
                                // 播放完成时恢复展示原生视频广告按钮
                                // 隐藏视频信息流视图
                                hideVideoInfoLayout();
                                // 释放资源
                                if (videoInfoViewBuilder != null) {
                                    videoInfoViewBuilder.release();
                                }
                                // 移除原生视频控件
                                if (mNativeVideoAdLayout != null) {
                                    mNativeVideoAdLayout.removeAllViews();
                                    mNativeVideoAdLayout.setVisibility(View
                                            .GONE);
                                }
                            }

                        }
                );
        if (mNativeVideoAdLayout != null) {
            final RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
            if (nativeVideoAdView != null) {
                mNativeVideoAdLayout.removeAllViews();
                // 添加原生视频广告
                mNativeVideoAdLayout.addView(nativeVideoAdView, params);
                mNativeVideoAdLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 展示视频信息流视图
     */
    private void showVideoInfoLayout() {
        if (mVideoInfoLayout != null && mVideoInfoLayout.getVisibility() != View.VISIBLE) {
            mVideoInfoLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏视频信息流视图
     */
    private void hideVideoInfoLayout() {
        if (mVideoInfoLayout != null && mVideoInfoLayout.getVisibility() != View.GONE) {
            mVideoInfoLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 原生视频广告
        VideoAdManager.getInstance(getApplicationContext()).onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 原生视频广告
        VideoAdManager.getInstance(getApplicationContext()).onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 原生视频广告
        VideoAdManager.getInstance(getApplicationContext()).onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 原生视频广告
        VideoAdManager.getInstance(getApplicationContext()).onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 原生视频广告
        VideoAdManager.getInstance(getApplicationContext()).onDestroy();
    }
}
