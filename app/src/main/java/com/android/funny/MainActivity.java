package com.android.funny;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.funny.component.ApplicationComponent;
import com.android.funny.ui.base.BaseActivity;
import com.android.funny.ui.base.SupportFragment;
import com.android.funny.ui.imageclassify.CarDetectFragment;
import com.android.funny.ui.imageclassify.ImageClassifyFragment;
import com.android.funny.ui.jandan.JanDanFragment;
import com.android.funny.ui.personal.PersonalFragment;
import com.android.funny.ui.news.NewsFragment;
import com.android.funny.ui.video.VideoFragment;
import com.android.funny.utils.StatusBarUtil;
import com.android.funny.widget.BottomBar;
import com.android.funny.widget.BottomBarTab;

import abc.abc.abc.nm.bn.BannerManager;
import abc.abc.abc.nm.bn.BannerViewListener;
import abc.abc.abc.nm.cm.ErrorCode;
import abc.abc.abc.nm.sp.SpotManager;
import abc.abc.abc.nm.vdo.VideoAdManager;
import abc.abc.abc.nm.vdo.VideoAdRequestListener;
import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.contentContainer)
    FrameLayout mContentContainer;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    private SupportFragment[] mFragments = new SupportFragment[4];


    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }


    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        // 预加载数据
        preloadData();
        //设置广告条
        setupBannerAd();
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
        if (savedInstanceState == null) {
            mFragments[0] = ImageClassifyFragment.newInstance();
            mFragments[1] = CarDetectFragment.newInstance();
            mFragments[2] = JanDanFragment.newInstance();
            mFragments[3] = PersonalFragment.newInstance();

            getSupportDelegate().loadMultipleRootFragment(R.id.contentContainer, 0,
                    mFragments[0],
                    mFragments[1],
                    mFragments[2],
                    mFragments[3]);
        } else {
            mFragments[0] = findFragment(NewsFragment.class);
            mFragments[1] = findFragment(VideoFragment.class);
            mFragments[2] = findFragment(JanDanFragment.class);
            mFragments[3] = findFragment(PersonalFragment.class);
        }

        mBottomBar.addItem(new BottomBarTab(this, R.drawable.ic_cai, "菜品"))
                .addItem(new BottomBarTab(this, R.drawable.ic_car, "汽车"))
                .addItem(new BottomBarTab(this, R.drawable.ic_gaoxiao, "搞笑"))
                .addItem(new BottomBarTab(this, R.drawable.ic_my, "我的"));
        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                getSupportDelegate().showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onBackPressedSupport() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressedSupport();
        if (SpotManager.getInstance(getApplicationContext())!= null) {
            SpotManager.getInstance(getApplicationContext()).onDestroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 展示广告条窗口的 onDestroy() 回调方法中调用
        BannerManager.getInstance(getApplicationContext()).onDestroy();

        // 退出应用时调用，用于释放资源
        // 如果无法保证应用主界面的 onDestroy() 方法被执行到，请移动以下接口到应用的退出逻辑里面调用

        // 插屏广告（包括普通插屏广告、轮播插屏广告、原生插屏广告）
        if (SpotManager.getInstance(getApplicationContext())!= null) {
            SpotManager.getInstance(getApplicationContext()).onAppExit();
        }
        // 视频广告（包括普通视频广告、原生视频广告）
        VideoAdManager.getInstance(getApplicationContext()).onAppExit();
    }

    /**
     * 预加载数据
     */
    private void preloadData() {
        // 设置服务器回调 userId，一定要在请求广告之前设置，否则无效
        VideoAdManager.getInstance(getApplicationContext()).setUserId("");
        // 请求视频广告
        // 注意：不必每次展示视频广告前都请求，只需在应用启动时请求一次
        VideoAdManager.getInstance(getApplicationContext())
                .requestVideoAd(getApplicationContext(), new VideoAdRequestListener() {

                    @Override
                    public void onRequestSuccess() {
                        Log.i("main_ad", "请求视频广告成功");
                    }

                    @Override
                    public void onRequestFailed(int errorCode) {
                        Log.e("main_ad","请求视频广告失败，errorCode"+ errorCode);
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                                break;
                            case ErrorCode.NON_AD:
                                Toast.makeText(getApplicationContext(), "暂无视频广告", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "请稍后再试", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    /**
     * 设置广告条广告
     */
    private void setupBannerAd() {
        /**
         * 悬浮布局
         */
        // 实例化LayoutParams
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置广告条的悬浮位置，这里示例为右下角
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        // 获取广告条
        final View bannerView = BannerManager.getInstance(getApplicationContext())
                .getBannerView(getApplicationContext(), new BannerViewListener() {

                    @Override
                    public void onRequestSuccess() {
                        Log.i("main_ad","请求广告条成功");

                    }

                    @Override
                    public void onSwitchBanner() {
                        Log.i("main_ad","广告条切换");
                    }

                    @Override
                    public void onRequestFailed() {
                        Log.i("main_ad","请求广告条失败");
                    }
                });
        // 添加广告条到窗口中
//        ((Activity) getApplicationContext()).addContentView(bannerView, layoutParams);
    }
}
