package com.android.funny.ui.jandan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.funny.R;
import com.android.funny.bean.Constants;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.ui.base.BaseFragment;
import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;
import com.qq.e.ads.nativ.MediaListener;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.NativeMediaAD;
import com.qq.e.ads.nativ.NativeMediaADData;
import com.qq.e.ads.nativ.NativeMediaAD.NativeMediaADListener;
import com.qq.e.comm.util.AdError;
import com.androidquery.callback.AjaxStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;

/**
 * Created by 14400 on 2018/2/25.
 */

public class AdListFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private String TAG = "AD_DEMO";
    private LinearLayoutManager mLinearLayoutManager;
    private CustomAdapter mAdapter;
    private List<NormalItem> mList = new ArrayList<NormalItem>();
    private NativeMediaADData mAD;
    private NativeMediaAD mADManager;

    public static final int MAX_ITEMS = 10;
    public static final int AD_COUNT = 3;   // 本示例演示加载1条广告
    public static int AD_POSITION = 1;      // 把广告摆在RecyclerView数据集的第2个位置

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
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        initData1();
    }

    @Override
    public void initData() {

    }

    private void initData1() {
        initNativeVideoAD();
        loadAD();
        for (int i = 0; i < MAX_ITEMS; ++i) {
            mList.add(new NormalItem("No." + i + " Normal Data"));
        }
        mAdapter = new CustomAdapter(mList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        // 监听滚动，调用增强型接口onScroll来管理视频的自动播放和停止
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLinearLayoutManager == null) {
                    return;
                }

                // 尽量有效地调用接口，发现广告在屏幕可见范围内滚动，才调用onScroll接口。
                int firstVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                int lastVisiblePosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (AD_POSITION >= firstVisiblePosition && AD_POSITION <= lastVisiblePosition
                        && mAD != null && mAD.isVideoAD() && mAD.isVideoLoaded()) {
                    mAD.onScroll(AD_POSITION, mRecyclerView);
                }
            }
        });
    }

    private void initNativeVideoAD() {
        NativeMediaADListener listener = new NativeMediaADListener() {

            @Override
            public void onADLoaded(List<NativeMediaADData> ads) {
                Toast.makeText(getContext(), "成功加载原生广告：" + ads.size() + "条",
                        Toast.LENGTH_SHORT).show();
                if (ads.size() > 0) {
                    mAD = ads.get(0);
                    preLoadVideo();
                    addADIntoList();
                }
            }

            @Override
            public void onNoAD(AdError adError) {
                Toast.makeText(getContext(),
                        String.format("广告加载失败，错误码：%d，错误信息：%s", adError.getErrorCode(), adError.getErrorMsg()),
                        Toast.LENGTH_SHORT).show();
                Log.i(TAG, "广告加载失败，错误码："+ adError.getErrorCode() + "错误信息:" + adError.getErrorMsg());
            }

            @Override
            public void onADStatusChanged(NativeMediaADData ad) {
                if (mAdapter != null && ad != null) {
                    /**
                     * 虽然RecyclerView提供了局部刷新单条Item的方法，但是不要直接调用mAdapter.notifyItemChanged(AD_POSITION)来更新广告item中的下载进度。
                     * 这样会引起onBindViewHolder重复调用，从而以导致视频播放反复重头播放、数据上报出现问题。
                     * 我们应该采用局部刷新的方法，仅更新下载进度显示的这个子View。这个地方的处理和ListView的处理是一样的。
                     */
                    mAdapter.updateDownloadingItem(ad);
                }
            }

            @Override
            public void onADError(NativeMediaADData adData, AdError adError) {
                Log.i(TAG, adData.getTitle() + " onADError, error code: " + adError.getErrorCode()
                        + ", error msg: " + adError.getErrorMsg());
            }

            @Override
            public void onADVideoLoaded(NativeMediaADData adData) {
                Log.i(TAG, adData.getTitle() + " ---> 视频加载完成"); // 仅仅是加载视频文件完成，如果没有绑定MediaView视频仍然不可以播放
            }

            @Override
            public void onADExposure(NativeMediaADData adData) {
                Log.i(TAG, adData.getTitle() + " onADExposure");
            }

            @Override
            public void onADClicked(NativeMediaADData adData) {
                Log.i(TAG, adData.getTitle() + " onADClicked");
            }
        };

        mADManager = new NativeMediaAD(getContext(), Constants.T_APPID, Constants.NativeVideoPosID, listener);
    }

    private void preLoadVideo() {
        if (mAD != null && mAD.isVideoAD()) {
            mAD.preLoadVideo();
        }
    }

    protected void addADIntoList() {
        if (mAD != null && mAdapter != null) {
            mAdapter.addADToPosition(AD_POSITION, mAD);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void loadAD() {
        if (mADManager != null) {
            try {
                mADManager.loadAD(AD_COUNT);
            } catch (Exception e) {
                Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        if (mAD != null) {
            mAD.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        if (mAD != null) {
            mAD.stop();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        if (mAD != null) {
            mAD.destroy();
        }
        super.onDestroy();
    }



    /**
     * your custom feeds item
     */
    public class NormalItem {
        private String title;

        public NormalItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        static final int TYPE_DATA = 0;
        static final int TYPE_AD = 1;

        private List<Object> mData;
        private TreeSet mADSet = new TreeSet();

        public CustomAdapter(List list, Activity act) {
            mData = list;
        }

        /** 更新item中的进度显示子View，而不是整个Item */
        public void updateDownloadingItem(NativeMediaADData ad) {
            if (ad != null) {
                ViewHolder viewHolder = mRecyclerView.findViewHolderForPosition(AD_POSITION);
                if (viewHolder instanceof CustomViewHolder) {
                    CustomViewHolder vh = (CustomViewHolder) viewHolder;
                    if (!ad.isAPP()) {
                        vh.download.setText("浏览");
                    } else {
                        switch (ad.getAPPStatus()) {
                            case 0:
                                vh.download.setText("下载");
                                break;
                            case 1:
                                vh.download.setText("启动");
                                break;
                            case 2:
                                vh.download.setText("更新");
                                break;
                            case 4:
                                vh.download.setText(ad.getProgress() + "%");
                                break;
                            case 8:
                                vh.download.setText("安装");
                                break;
                            case 16:
                                vh.download.setText("下载失败，重新下载");
                                break;
                            default:
                                vh.download.setText("浏览");
                                break;
                        }
                    }
                }
            }
        }

        public void addADToPosition(int position, NativeMediaADData ad) {
            if (position >= 0 && position < mData.size() && ad != null) {
                mData.add(position, ad);
                mADSet.add(position);
            }
        }

        @Override
        public int getItemCount() {
            if (mData != null) {
                return mData.size();
            } else {
                return 0;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mADSet.contains(position)) {
                return TYPE_AD;
            }
            return TYPE_DATA;
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {
            int type = getItemViewType(position);
            if (TYPE_AD == type) {
                final NativeMediaADData ad = (NativeMediaADData) mData.get(position);

                if (!ad.isAPP()) {
                    customViewHolder.download.setText("浏览");
                } else {
                    switch (ad.getAPPStatus()) {
                        case 0:
                            customViewHolder.download.setText("下载");
                            break;
                        case 1:
                            customViewHolder.download.setText("启动");
                            break;
                        case 2:
                            customViewHolder.download.setText("更新");
                            break;
                        case 4:
                            customViewHolder.download.setText(ad.getProgress() + "%");
                            break;
                        case 8:
                            customViewHolder.download.setText("安装");
                            break;
                        case 16:
                            customViewHolder.download.setText("下载失败，重新下载");
                            break;
                        default:
                            customViewHolder.download.setText("浏览");
                            break;
                    }
                }

                customViewHolder.logoAQ.id(R.id.img_logo).image(TextUtils.isEmpty(ad.getIconUrl())? ad.getImgUrl() : ad.getIconUrl(), false, true);
                customViewHolder.name.setText(ad.getTitle());
                customViewHolder.desc.setText(ad.getDesc());
                /**
                 * 注意：在UI渲染时，必须先调用onExposured接口曝光广告，否则点击接口onClicked将无效。
                 */
                ad.onExposured(customViewHolder.adInfoContainer);
                customViewHolder.download.setOnClickListener(v -> ad.onClicked(v));

                // 预设大图，后面检查到视频加载完成后，再隐藏大图，显示MediaView
                customViewHolder.logoAQ.id(R.id.img_poster).image((String) ad.getImgUrl(), false, true, 0, 0, new BitmapAjaxCallback() {
                    @Override
                    protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                        // AQuery框架有一个问题，就是即使在图片加载完成之前将ImageView设置为了View.GONE，在图片加载完成后，这个ImageView会被重新设置为VIEW.VISIBLE。
                        // 所以在这里需要判断一下，如果已经把ImageView设置为隐藏，开始播放视频了，就不要再显示广告的大图。开发者在用其他的图片加载框架时，也应该注意检查下是否有这个问题。
                        if (iv.getVisibility() == View.VISIBLE) {
                            iv.setImageBitmap(bm);
                        }
                    }
                });
                customViewHolder.mediaView.setVisibility(View.GONE);
                customViewHolder.poster.setVisibility(View.VISIBLE);

                // 如果广告对象是视频广告，且视频素材加载完成了，有两种方式可以渲染：1、自动播放；2、手动播放；
                if (ad.isVideoAD() && ad.isVideoLoaded()) {
                    // 1、自动播放的方式：step 1.隐藏广告大图，显示MediaView；step 2.再给广告对象绑定MediaView；step 3.播放视频
                    // customViewHolder.mediaView.setVisibility(View.VISIBLE);
                    // customViewHolder.poster.setVisibility(View.GONE);
                    // ad.bindView(customViewHolder.mediaView, true);
                    // ad.play();
                    // end - 自动播放
                    // 注意：原生视频广告提供setVolumeOn(boolean flag)接口来设置开始播放时是否有声音。一般在消息流中都是静音+自动播放。


                    // 2、手动播放的方式：在广告大图上预先显示一个播放按钮，让用户点击之后，再隐藏大图和播放按钮，显示出MediaView来播放广告。
                    customViewHolder.play.setVisibility(View.VISIBLE); // 显示播放按钮
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { // 给大图设点透明度
                        customViewHolder.poster.setAlpha(0.85f);
                    }

                    customViewHolder.play.setOnClickListener(new OnClickListener() { // 监听点击，隐藏大图、播放按钮，显示MediaView，播放视频
                        @Override
                        public void onClick(View v) {
                            customViewHolder.mediaView.setVisibility(View.VISIBLE);
                            customViewHolder.poster.setVisibility(View.GONE);
                            customViewHolder.play.setVisibility(View.GONE);
                            ad.bindView(customViewHolder.mediaView, true);
                            ad.play();
                            ad.setVolumeOn(true);
                        }
                    });
                    // end-手动播放的方式
                    // 注意：原生视频广告提供setVolumeOn(boolean flag)接口来设置开始播放时是否有声音。一般在消息流中都是有声+手动播放。


                    // 监听广告的视频播放状态
                    ad.setMediaListener(new MediaListener() {

                        @Override
                        public void onVideoReady(long videoDuration) {
                            Log.i(TAG, "onVideoReady, video duration = " + videoDuration);
                        }

                        @Override
                        public void onVideoStart() {
                            Log.i(TAG, "onVideoStart");
                        }

                        @Override
                        public void onVideoPause() {
                            Log.i(TAG, "onVideoPause");
                        }

                        @Override
                        public void onVideoComplete() {
                            Log.i(TAG, "onVideoComplete");
                        }

                        @Override
                        public void onVideoError(AdError adError) {
                            Log.i(TAG, String.format("onVideoError, errorCode: %d, errorMsg: %s",
                                    adError.getErrorCode(), adError.getErrorMsg()));
                        }

                        @Override
                        public void onReplayButtonClicked() {
                            Log.i(TAG, "onReplayButtonClicked");
                        }

                        @Override
                        public void onADButtonClicked() {
                            Log.i(TAG, "onADButtonClicked");
                        }

                        @Override
                        public void onFullScreenChanged(boolean inFullScreen) {
                            Log.i(TAG, "onFullScreenChanged, inFullScreen = " + inFullScreen);
                        }
                    });
                }
            } else {
                customViewHolder.title.setText(((NormalItem) mData.get(position)).getTitle());
            }
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            int layoutId = (viewType == TYPE_AD) ? R.layout.item_ad : R.layout.item_data;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, null);
            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        class CustomViewHolder extends ViewHolder {
            public TextView title;
            public MediaView mediaView;
            public RelativeLayout adInfoContainer;
            public TextView name, desc;
            public ImageView logo, poster;
            public Button download, play;
            public AQuery logoAQ;

            public CustomViewHolder(View view) {
                super(view);
                // normal view
                title = (TextView) view.findViewById(R.id.title);
                // ad view
                mediaView = (MediaView) view.findViewById(R.id.gdt_media_view);
                poster = (ImageView) view.findViewById(R.id.img_poster);
                adInfoContainer = (RelativeLayout) view.findViewById(R.id.ad_info);
                logo = (ImageView) view.findViewById(R.id.img_logo);
                name = (TextView) view.findViewById(R.id.text_title);
                desc = (TextView) view.findViewById(R.id.text_desc);
                download = (Button) view.findViewById(R.id.btn_download);
                play = (Button) view.findViewById(R.id.btn_play);
                logoAQ = new AQuery(view);
            }

        }
    }
}
