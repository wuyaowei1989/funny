package com.android.funny;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.funny.component.ApplicationComponent;
import com.android.funny.ui.base.BaseActivity;
import com.android.funny.utils.PermissionHelper;

import java.util.concurrent.TimeUnit;

import abc.abc.abc.nm.cm.ErrorCode;
import abc.abc.abc.nm.sp.SplashViewSettings;
import abc.abc.abc.nm.sp.SpotListener;
import abc.abc.abc.nm.sp.SpotManager;
import abc.abc.abc.nm.sp.SpotRequestListener;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;


public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.iv_ad)
    RelativeLayout ivAd;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.fl_ad)
    FrameLayout flAd;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private PermissionHelper mPermissionHelper;

    private String TAG = "WelcomeActivity";

    @Override
    public int getContentLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
                runApp();
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
            runApp();
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
                Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
                runApp();
            } else {
                // 如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
        // 开屏展示界面的 onDestroy() 回调方法中调用
        SpotManager.getInstance(getApplicationContext()).onDestroy();
    }

    private void toMain() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public Observable<Integer> countDown(int time) {
        if (time < 0) time = 0;
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime + 1);
    }


    @OnClick(R.id.fl_ad)
    public void onViewClicked() {
        toMain();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onRetry() {

    }

    /**
     * 跑应用的逻辑
     */
    private void runApp() {
        preloadAd();
        setupSplashAd(); // 如果需要首次展示开屏，请注释掉本句代码
    }

    /**
     * 预加载广告
     */
    private void preloadAd() {
        // 注意：不必每次展示插播广告前都请求，只需在应用启动时请求一次
        SpotManager.getInstance(getApplicationContext()).requestSpot(new SpotRequestListener() {
            @Override
            public void onRequestSuccess() {
                Log.i(TAG, "请求插播广告成功");
                //				// 应用安装后首次展示开屏会因为本地没有数据而跳过
                //              // 如果开发者需要在首次也能展示开屏，可以在请求广告成功之前展示应用的logo，请求成功后再加载开屏
                //				setupSplashAd();
            }

            @Override
            public void onRequestFailed(int errorCode) {
                Log.i(TAG, "请求插播广告失败，errorCode: " + errorCode);
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
                toMain();
            }
        });
    }

    /**
     * 设置开屏广告
     */
    private void setupSplashAd() {

        // 对开屏进行设置
        SplashViewSettings splashViewSettings = new SplashViewSettings();
        //		// 设置是否展示失败自动跳转，默认自动跳转
        //		splashViewSettings.setAutoJumpToTargetWhenShowFailed(false);
        // 设置跳转的窗口类
        splashViewSettings.setTargetClass(MainActivity.class);
        // 设置开屏的容器
        splashViewSettings.setSplashViewContainer(ivAd);

        // 展示开屏广告
        SpotManager.getInstance(getApplicationContext())
                .showSplash(getApplicationContext(), splashViewSettings, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
                        Log.i(TAG, "开屏展示成功");
                        mCompositeDisposable.add(countDown(3).doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                tvSkip.setText("跳过 4");
                            }
                        }).subscribeWith(new DisposableObserver<Integer>() {
                            @Override
                            public void onNext(Integer integer) {
                                tvSkip.setText("跳过 " + (integer + 1));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                toMain();
                            }
                        }));
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
                        Log.i(TAG,"开屏展示失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                Log.i(TAG,"网络异常");
                                break;
                            case ErrorCode.NON_AD:
                                Log.i(TAG,"暂无开屏广告");
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
                                Log.i(TAG,"开屏资源还没准备好");
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
                                Log.i(TAG,"开屏展示间隔限制");
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                Log.i(TAG,"开屏控件处在不可见状态");
                                break;
                            default:
                                Log.i(TAG,"errorCode: "+ errorCode);
                                break;
                        }
                        toMain();
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.i(TAG,"开屏被关闭");
                        toMain();
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
                        Log.i(TAG,"开屏被点击");
                        Log.i(TAG,"是否是网页广告？" + (isWebPage ? "是" : "不是"));
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
    }

}
