package com.android.funny.ui.imageclassify;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.funny.R;
import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.bean.BaiduPicBean;
import com.android.funny.bean.CarDetectBean;
import com.android.funny.bean.Constants;
import com.android.funny.bean.DishDetectBean;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.component.DaggerHttpComponent;
import com.android.funny.ui.adapter.CustomBaseQuickAdapter;
import com.android.funny.ui.base.BaseFragment;
import com.android.funny.ui.imageclassify.contract.ImageClassifyContract;
import com.android.funny.ui.imageclassify.presenter.ImageClassifyPresenter;
import com.android.funny.utils.BitmapUtils;
import com.android.funny.utils.ImageLoaderUtil;
import com.android.funny.utils.ShareUtils;
import com.android.funny.widget.CommonAdapter;
import com.android.funny.widget.flingswipe.SwipeFlingAdapterView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/1/31.
 */

public class ImageClassifyFragment extends BaseFragment<ImageClassifyPresenter> implements ImageClassifyContract.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.frame)
    SwipeFlingAdapterView frame;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.empty_img)
    ImageView emptyImg;
    @BindView(R.id.empty_layout)
    ImageView emptyLayout;

    Unbinder unbinder;

    private static final int PICK_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;

    private String mAccessToken = "";
    private File mPhotoFile;

    private static final String IMAGE_FILE_NAME = "photo.png";
    private static final String IMAGE_FILE_PATH = Environment.getExternalStorageDirectory() + "/photo.png";

    CustomBaseQuickAdapter<DishDetectBean.ResultBean> mAdapter;
    List<DishDetectBean.ResultBean> mDetectDataList = new ArrayList<>();
    ArrayList<BaiduPicBean.DataBean> mDataList = new ArrayList<>();
    CommonAdapter<BaiduPicBean.DataBean> mCardAdapter;

    public static ImageClassifyFragment newInstance() {
        Bundle args = new Bundle();
        ImageClassifyFragment fragment = new ImageClassifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_image_classify;
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
        mPresenter.getAccessToken(Constants.BAIDU_AI_AK, Constants.BAIDU_AI_SK);
        setCardAdapter();
        mAdapter = new CustomBaseQuickAdapter<DishDetectBean.ResultBean>(R.layout.item_img_classify, null) {
            @Override
            public void customConvert(BaseViewHolder holder, DishDetectBean.ResultBean item) {
                holder.setText(R.id.dish_name, item.getName())
                        .setText(R.id.dish_calorie, item.getCalorie())
                        .setText(R.id.dish_probability, item.getProbability());
                holder.addOnClickListener(R.id.share_tv);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShareUtils.shareText(getContext(), mDetectDataList.get(position).getName());
            }
        });
        Random random = new Random();
        mPresenter.getImageList("高清美食图片", random.nextInt(10), 20);
    }

    private void setCardAdapter() {
        mCardAdapter = new CommonAdapter<BaiduPicBean.DataBean>(getContext(), mDataList, R.layout.item_image_card) {
            @Override
            protected void setListeners(com.android.funny.widget.BaseViewHolder holder, View view, int position) {
                view.setOnClickListener(holder);
            }

            @Override
            protected void setViewData(int position, com.android.funny.widget.BaseViewHolder holder, BaiduPicBean.DataBean item) {
                ImageView imageView = holder.getView(R.id.img);
                ImageLoaderUtil.LoadImage(mContext, item.getObjURL(), imageView);
            }

            @Override
            public void onClickBack(int position, View view, com.android.funny.widget.BaseViewHolder holder) {
            }
        };

        frame.setAdapter(mCardAdapter);
        frame.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                mDataList.remove(0);
                mAdapter.setNewData(null);
                mCardAdapter.update(mDataList);
            }

            @Override
            public void onLeftCardExit(Object o) {

            }

            @Override
            public void onRightCardExit(Object o) {

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                if (mDataList.size() == 1) {
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyImg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(float v) {
            }
        });
        frame.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                if (dataObject == null) {
                    //todo
                } else {
                    mAdapter.setNewData(null);
                    showLoadingDialog();
                    dishDetect((BaiduPicBean.DataBean) dataObject);
                }
            }
        });
    }

    public void dishDetect(BaiduPicBean.DataBean dataBean) {
        Glide.with(getContext()).asBitmap().load(dataBean.getObjURL()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                String img = BitmapUtils.base64Encode(BitmapUtils.bitmapToByte(resource));
                mPresenter.dishDetect(mAccessToken, img, 3, 0.95f);
            }
        });

    }

    @Override
    public void initData() {
        showSuccess();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        mPhotoFile = new File(IMAGE_FILE_PATH);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showDialog() {
        List<String> data = new ArrayList<>();
        data.add("相机");
        data.add("相册");
        data.add("获取网络图片");
        data.add("取消");
        StyledDialog.buildIosSingleChoose(data, new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence charSequence, int i) {
                switch (i) {
                    case 0:
                        takePhoto();
                        StyledDialog.dismiss();
                        break;
                    case 1:
                        photoAlbum();
                        StyledDialog.dismiss();
                        break;
                    case 2:
                        emptyLayout.setImageBitmap(null);
                        Random random = new Random();
                        mPresenter.getImageList("高清美食图片", random.nextInt(10), 30);
                        StyledDialog.dismiss();
                        break;
                    case 3:
                        StyledDialog.dismiss();
                        break;
                }
            }
        }).show();
    }

    private void takePhoto() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //相机
                    if (BitmapUtils.isSdcardExisting()) {
                        Intent intent_camera = new Intent(
                                "android.media.action.IMAGE_CAPTURE");
                        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT,
                                ImageClassifyFragment.this.getImageUri());
                        ImageClassifyFragment.this.startActivityForResult(intent_camera,
                                CAMERA_REQUEST_CODE);

                    } else {
                        Toast.makeText(ImageClassifyFragment.this.getActivity(), "请插入sd卡",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //未获取权限
                    Toast.makeText(ImageClassifyFragment.this.getContext(), "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void photoAlbum() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //从相册选择
                    Intent intent_gallery = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent_gallery
                            .setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*");
                    ImageClassifyFragment.this.startActivityForResult(intent_gallery,
                            PICK_REQUEST_CODE);
                } else {
                    //未获取权限
                    Toast.makeText(ImageClassifyFragment.this.getContext(), "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //获取图片的uri
    private Uri getImageUri() {
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_NAME));
    }

    // 在从拍照或者图片选择后返回
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            Bitmap image;
            switch (requestCode) {
                case PICK_REQUEST_CODE:
                    Uri uri = data.getData();
                    //保存图片
                    BitmapUtils.saveImageFromGallery(getContext(), uri, mPhotoFile);
                    //显示图片
                    image = BitmapUtils.compressImage(IMAGE_FILE_PATH, 400, 500);
                    detectManualSelPic(image);
                    break;
                case CAMERA_REQUEST_CODE:
                    BitmapUtils.saveImageFromCamera(IMAGE_FILE_PATH, mPhotoFile);
                    image = BitmapUtils.compressImage(IMAGE_FILE_PATH, 400, 500);
                    detectManualSelPic(image);
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void detectManualSelPic(Bitmap bitmap) {
        mDataList.clear();
        mCardAdapter.update(mDataList);
        emptyLayout.setVisibility(View.VISIBLE);
        emptyLayout.setImageBitmap(bitmap);
        String img = BitmapUtils.base64Encode(BitmapUtils.bitmapToByte(bitmap));
        showLoadingDialog();
        mPresenter.dishDetect(mAccessToken, img, 5, 0.95f);
    }


    @Override
    public void loadAccessTokenData(BaiduAccessTokenBean bean) {
        mAccessToken = bean.getAccess_token();
    }

    @Override
    public void loadAccessTokenDataFail(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loaDishDetectData(DishDetectBean bean) {
        mDetectDataList.clear();
        mDetectDataList.addAll(bean.getResult());
        hideLoadingDialog();
        mAdapter.setNewData(bean.getResult());
    }

    @Override
    public void loadCarDetectData(CarDetectBean bean) {

    }

    @Override
    public void loadPlantDetectData(Object o) {

    }

    @Override
    public void loadImageData(BaiduPicBean baiduPicBean) {
        mDataList.clear();
        mDataList.addAll(baiduPicBean.getData());
        emptyImg.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        mCardAdapter.update(mDataList);
    }

    @OnClick(R.id.fab)
    public void onViewFabClicked() {
        showDialog();
    }

    @OnClick(R.id.empty_img)
    public void onViewClicked() {
        showDialog();
    }
}
