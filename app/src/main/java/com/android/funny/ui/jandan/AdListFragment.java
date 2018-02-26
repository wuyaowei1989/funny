package com.android.funny.ui.jandan;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.funny.R;
import com.android.funny.bean.Constants;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.ui.adapter.CustomBaseQuickAdapter;
import com.android.funny.ui.base.BaseFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by 14400 on 2018/2/25.
 */

public class AdListFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrFrameLayout mPtrFrameLayout;

    private String TAG = "AD_DEMO";
    private Boolean mIsRefresh = false;
    private CustomBaseQuickAdapter<NativeExpressADView> mAdapter;
    private NativeExpressAD nativeExpressAD;
    private List<NativeExpressADView> mDataList = new ArrayList<>();
    private NativeExpressAD.NativeExpressADListener mNativeExpressADListener;

    public static final int AD_COUNT = 3;   // 本示例演示加载1条广告

    public static AdListFragment newInstance() {
        Bundle args = new Bundle();
        AdListFragment fragment = new AdListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_ad_list;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        mNativeExpressADListener = new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(AdError adError) {

            }

            @Override
            public void onADLoaded(List<NativeExpressADView> adList) {
                Log.i(TAG, "onADLoaded: " + adList.size());
                mDataList.addAll(adList);
                setData(mIsRefresh, adList);
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {

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

        mAdapter = new CustomBaseQuickAdapter<NativeExpressADView>(R.layout.ad_header_view, null) {
            @Override
            public void customConvert(BaseViewHolder holder, NativeExpressADView item) {
                ViewGroup container = (ViewGroup) holder.getView(R.id.container);
                ViewGroup parent = (ViewGroup)item.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                // 广告可见才会产生曝光，否则将无法产生收益。
                container.addView(item);
                item.render();
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(() -> loadMore(), mRecyclerView);

        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mIsRefresh = true;
                refreshAd();
            }
        });

        refreshAd();

    }

    private void loadMore() {
        refreshAd();
    }

    private void refreshAd() {

        /**
         *  如果选择支持视频的模版样式，请使用{@link Constants#NativeExpressSupportVideoPosID}
         */
        nativeExpressAD = new NativeExpressAD(getContext(), getMyADSize(), Constants.T_APPID, Constants.NativeVideoPosID, mNativeExpressADListener); // 这里的Context必须为Activity
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // 设置什么网络环境下可以自动播放视频
                .setAutoPlayMuted(true) // 设置自动播放视频时，是否静音
                .build()); // setVideoOption是可选的，开发者可根据需要选择是否配置
        nativeExpressAD.loadAD(AD_COUNT);
    }

    private ADSize getMyADSize() {
        int w = ADSize.FULL_WIDTH;
        int h = ADSize.AUTO_HEIGHT;
        return new ADSize(w, h);
    }

    @Override
    public void initData() {

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

    private void setData(boolean isRefresh, List data) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
            mAdapter.setEnableLoadMore(true);
            mIsRefresh = false;
            mPtrFrameLayout.refreshComplete();
        } else {
            if (size > 0) {
                mAdapter.addData(data);
                mAdapter.loadMoreComplete();
            }
        }
    }
}
