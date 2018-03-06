package com.android.funny.ui.jandan;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.android.funny.R;
import com.android.funny.bean.FreshNewsBean;
import com.android.funny.bean.JdDetailBean;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.component.DaggerHttpComponent;
import com.android.funny.ui.base.BaseFragment;
import com.android.funny.widget.CustomLoadMoreView;

import abc.abc.abc.nm.cm.ErrorCode;
import abc.abc.abc.nm.vdo.VideoAdListener;
import abc.abc.abc.nm.vdo.VideoAdManager;
import abc.abc.abc.nm.vdo.VideoAdSettings;
import abc.abc.abc.nm.vdo.VideoInfoViewBuilder;
import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

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
    /**
     * 原生视频广告控件容器
     */
    private RelativeLayout mNativeVideoAdLayout;
    /**
     * 视频信息栏容器
     */
    private RelativeLayout mVideoInfoLayout;

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
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
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
            }
        }, mRecyclerView);

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
        final VideoInfoViewBuilder videoInfoViewBuilder = VideoAdManager.getInstance(mContext)
                .getVideoInfoViewBuilder(
                        mContext).setRootContainer(
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
        View nativeVideoAdView = VideoAdManager.getInstance(mContext)
                .getNativeVideoAdView(mContext,
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
                                Toast.makeText(getContext(), "播放视频被中断", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ErrorCode.NON_AD:
                                        Toast.makeText(getContext(), "原生视频暂无广告", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ErrorCode.RESOURCE_NOT_READY:
                                        Toast.makeText(getContext(),"原生视频资源还没准备好", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ErrorCode.SHOW_INTERVAL_LIMITED:
                                        Toast.makeText(getContext(),"请勿频繁展示", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ErrorCode
                                            .WIDGET_NOT_IN_VISIBILITY_STATE:
                                        Toast.makeText(getContext(),"原生视频控件处在不可见状态", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getContext(),"请稍后再试", Toast.LENGTH_SHORT).show();
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
        VideoAdManager.getInstance(mContext).onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 原生视频广告
        VideoAdManager.getInstance(mContext).onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 原生视频广告
        VideoAdManager.getInstance(mContext).onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 原生视频广告
        VideoAdManager.getInstance(mContext).onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 原生视频广告
        VideoAdManager.getInstance(mContext).onDestroy();
    }
}
