package com.android.funny.ui.personal;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.funny.R;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.ui.base.BaseFragment;

import abc.abc.abc.nm.cm.ErrorCode;
import abc.abc.abc.nm.sp.SpotListener;
import abc.abc.abc.nm.sp.SpotManager;
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
        // 设置轮播插屏广告
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tvUrl, R.id.tvGithubUrl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvUrl:
                setupSlideableSpotAd();
                break;
            case R.id.tvGithubUrl:
                toWeb(getResources().getString(R.string.githubUrl));
                break;

        }
    }

    private void toWeb(String url) {
        Uri weburl = Uri.parse(url);
        Intent web_Intent = new Intent(Intent.ACTION_VIEW, weburl);
        getActivity().startActivity(web_Intent);
    }

    /**
     * 设置轮播插屏广告
     */
    private void setupSlideableSpotAd() {
        // 设置插屏图片类型，默认竖图
        //		// 横图
        //		SpotManager.getInstance(mContext).setImageType(SpotManager
        // .IMAGE_TYPE_HORIZONTAL);
        // 竖图
        SpotManager.getInstance(mContext).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

        // 设置动画类型，默认高级动画
        //		// 无动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        //				.ANIMATION_TYPE_NONE);
        //		// 简单动画
        //		SpotManager.getInstance(mContext)
        //		                    .setAnimationType(SpotManager.ANIMATION_TYPE_SIMPLE);
        // 高级动画
        SpotManager.getInstance(mContext)
                .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

        // 展示轮播插屏广告
        SpotManager.getInstance(mContext)
                .showSlideableSpot(mContext, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
                        Log.i("banner_ad", "轮播插屏展示成功");
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
                        Log.e("banner_ad", "轮播插屏展示失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                                break;
                            case ErrorCode.NON_AD:
                                Toast.makeText(getContext(), "暂无轮播插屏广告", Toast.LENGTH_SHORT).show();
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
                                Toast.makeText(getContext(), "轮播插屏资源还没准备好", Toast.LENGTH_SHORT).show();
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
                                Toast.makeText(getContext(), "请勿频繁展示", Toast.LENGTH_SHORT).show();
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                Toast.makeText(getContext(), "请设置插屏为可见状态", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getContext(), "请稍后再试", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.i("banner_ad", "轮播插屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
                        Log.i("banner_ad", "轮播插屏被点击");
                        Log.i("banner_ad", "是否是网页广告" + (isWebPage ? "是" : "不是"));
                    }
                });
    }

    @Override
    public boolean onBackPressedSupport() {
        // 点击后退关闭轮播插屏广告
        if (SpotManager.getInstance(mContext).isSlideableSpotShowing()) {
            SpotManager.getInstance(mContext).hideSlideableSpot();
            return true;
        } else {
            return super.onBackPressedSupport();
        }
    }
}
