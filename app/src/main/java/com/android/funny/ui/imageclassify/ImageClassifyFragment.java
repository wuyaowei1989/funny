package com.android.funny.ui.imageclassify;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.funny.R;
import com.android.funny.bean.BaiduAccessTokenBean;
import com.android.funny.bean.Constants;
import com.android.funny.component.ApplicationComponent;
import com.android.funny.component.DaggerHttpComponent;
import com.android.funny.ui.base.BaseFragment;
import com.android.funny.ui.imageclassify.contract.ImageClassifyContract;
import com.android.funny.ui.imageclassify.presenter.ImageClassifyPresenter;
import com.android.funny.utils.BitmapUtils;
import com.baidu.aip.imageclassify.AipImageClassify;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

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
    @BindView(R.id.image_tv)
    TextView imageTv;
    Unbinder unbinder;

    private static final int PICK_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int ZOOM_REQUEST_CODE = 2;

    private static final String IMAGE_FILE_NAME = "photo.jpg";

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
        AipImageClassify client = new AipImageClassify(Constants.BAIDU_AI_APPID, Constants.BAIDU_AI_AK, Constants.BAIDU_AI_SK);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        HashMap<String, String> options = new HashMap<String, String>();
        options.put("top_num", "3");
        String image = "src/test.jpg";
        JSONObject res = client.dishDetect(image, options);
    }

    @Override
    public void initData() {

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
                break;
            case R.id.upload_img_tv:
                break;
        }
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
                        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                        intent_camera.putExtra(
                                MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        startActivityForResult(intent_camera,
                                CAMERA_REQUEST_CODE);

                    } else {
                        Toast.makeText(getActivity(), "请插入sd卡",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //未获取权限
                    Toast.makeText(getContext(), "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
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
            Bitmap photo_round = BitmapUtils.toRoundBitmap(photo);
            // saveServer();保存到服务器；
            // saveSD();保存到sd卡;
            BitmapUtils.saveToSDBitmap(getActivity(), "zmIcon.png",
                    photo_round);
        }
    }

    @Override
    public void loadAccessTokenData(BaiduAccessTokenBean bean) {

    }

    @Override
    public void loadAccessTokenDataFail(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loaDishDetectData(Object o) {

    }
}
