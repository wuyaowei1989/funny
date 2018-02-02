package com.android.funny.ui.imageclassify;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.funny.R;
import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.bean.Constants;
import com.android.funny.bean.DishDetectBean;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.component.DaggerHttpComponent;
import com.android.funny.ui.adapter.CustomBaseQuickAdapter;
import com.android.funny.ui.base.BaseFragment;
import com.android.funny.ui.imageclassify.contract.ImageClassifyContract;
import com.android.funny.ui.imageclassify.presenter.ImageClassifyPresenter;
import com.android.funny.utils.BitmapUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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


    @BindView(R.id.image_iv)
    ImageView imageIv;
    @BindView(R.id.image_layout)
    RelativeLayout imageLayout;
    @BindView(R.id.upload_img_tv)
    TextView uploadImgTv;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.upload_icon)
    LinearLayout uploadIcon;
    Unbinder unbinder;

    private static final int PICK_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int ZOOM_REQUEST_CODE = 2;
    private String mAccessToken = "";

    private static final String IMAGE_FILE_NAME = "photo.jpg";

    CustomBaseQuickAdapter<DishDetectBean.ResultBean> mAdapter;
    List<DishDetectBean.ResultBean> mDataList = new ArrayList<>();

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
        mAdapter = new CustomBaseQuickAdapter<DishDetectBean.ResultBean>(R.layout.item_img_classify, null) {
            @Override
            public void customConvert(BaseViewHolder holder, DishDetectBean.ResultBean item) {
                holder.setText(R.id.dish_name, item.getName())
                        .setText(R.id.dish_calorie, item.getCalorie())
                        .setText(R.id.dish_probability, item.getProbability());
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
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
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.image_layout, R.id.upload_img_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_layout:
                mPresenter.getAccessToken(Constants.BAIDU_AI_AK, Constants.BAIDU_AI_SK);
                break;
            case R.id.upload_img_tv:
                mPresenter.getImageList("美食", 0, 10);
                break;
        }
    }

    private void showDialog() {
        List<String> data = new ArrayList<>();
        data.add("相机");
        data.add("相册");
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
                        intent_camera.putExtra(
                                MediaStore.EXTRA_VIDEO_QUALITY, 0);
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
            switch (requestCode) {
                case PICK_REQUEST_CODE:
                    // 从图片选择图片后，直接返回的是uri，然后对通过uri取得图片，进行剪切；
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    // 首先判断sd卡是否挂载；
                    if (BitmapUtils.isSdcardExisting()) {
                        startPhotoZoom(getImageUri());
                    } else {
                        Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }
                    break;
                // 启动剪切图片；
                case ZOOM_REQUEST_CODE:
                    if (data != null) {
                        showImage(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 对图片进行剪切；
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 360);
        intent.putExtra("outputY", 360);
        //intent.putExtra("circleCrop", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, ZOOM_REQUEST_CODE);
    }

    // 最张显示图片；但是在实际中，在进入activity中要判断sd卡中是否有图片，如果有图片要首先显示图片。
    // 把图片保存到本地，并且上传给服务器；
    private void showImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            // saveServer();保存到服务器；
            // saveSD();保存到sd卡;
//            BitmapUtils.saveToSDBitmap(getActivity(), "pic.png",
//                    photo);
            String img = BitmapUtils.base64Encode(BitmapUtils.bitmapToByte(photo));
            uploadIcon.setVisibility(View.GONE);
            imageIv.setImageBitmap(photo);
            mPresenter.dishDetect(mAccessToken, img, 5, 0.95f);
        }
    }

    @Override
    public void loadAccessTokenData(BaiduAccessTokenBean bean) {
        mAccessToken = bean.getAccess_token();
        showDialog();
    }

    @Override
    public void loadAccessTokenDataFail(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loaDishDetectData(DishDetectBean bean) {
        mAdapter.setNewData(bean.getResult());
    }

    @Override
    public void loadCarDetectData(Object o) {

    }

    @Override
    public void loadPlantDetectData(Object o) {

    }

    @Override
    public void loadImageData() {

    }
}
