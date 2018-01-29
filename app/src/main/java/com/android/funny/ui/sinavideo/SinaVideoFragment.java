package com.android.funny.ui.sinavideo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.funny.R;
import com.android.funny.bean.MoveListBean;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.component.DaggerHttpComponent;
import com.android.funny.ui.adapter.VideoPagerAdapter;
import com.android.funny.ui.base.BaseFragment;
import com.android.funny.ui.sinavideo.contract.SinaVideoContract;
import com.android.funny.ui.sinavideo.presenter.SinaVideoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/27.
 */

public class SinaVideoFragment extends BaseFragment<SinaVideoPresenter> implements SinaVideoContract.View {

    private static final String TAG = "SinaVideoFragment";

    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    private VideoPagerAdapter mVideoPagerAdapter;

    public static SinaVideoFragment newInstance() {
        Bundle args = new Bundle();
        SinaVideoFragment fragment = new SinaVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_video;
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

    }

    @Override
    public void initData() {
        mPresenter.getMoveList("郑州", 0, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadMoveList(MoveListBean listBean) {
    }
}
