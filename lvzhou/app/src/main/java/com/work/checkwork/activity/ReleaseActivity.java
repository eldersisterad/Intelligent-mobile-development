package com.work.checkwork.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.work.checkwork.BaseActivity;
import com.work.checkwork.R;
import com.work.checkwork.dao.DBService;
import com.work.checkwork.model.DataBean;
import com.work.checkwork.model.UserBean;
import com.work.checkwork.preferences.AppPreferences;
import com.work.checkwork.utils.AppConstant;
import com.work.checkwork.utils.GlideEngine;

import org.json.JSONException;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;


public class ReleaseActivity extends BaseActivity {
    private static final String TAG = "ReleaseActivity";

    private TextView mTitle;
    private EditText mEditDescribe;
    private ImageView mSelectImg;
    private RadioButton mRadio0, mRadio1, mRadio2, mRadio3, mRadio4, mRadio5, mRadio6;

    private UserBean mUserBean;
    private DataBean mDataBean;
    private int type = 0;
    private String imgUrl;
    private UploadManager mUploadManager;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.release_activity;
    }

    @Override
    protected void onViewCreated() {
        mUserBean = AppPreferences.getParcelableEntity(this, AppConstant.EXTRA_USERINFO, UserBean.class);
        mTitle = findViewById(R.id.textview_title);
        mTitle.setText("发布");
        mEditDescribe = findViewById(R.id.edit_describe);
        mSelectImg = findViewById(R.id.select_img);
        mRadio0 = findViewById(R.id.radio_0);
        mRadio1 = findViewById(R.id.radio_1);
        mRadio2 = findViewById(R.id.radio_2);
        mRadio3 = findViewById(R.id.radio_3);
        mRadio4 = findViewById(R.id.radio_4);
        mRadio5 = findViewById(R.id.radio_5);
        mRadio6 = findViewById(R.id.radio_6);

        //config配置上传参数
        Configuration config = new Configuration.Builder()  //https://developer.qiniu.com/kodo/sdk/1236/android
                .chunkSize(512 * 1024)
                .connectTimeout(30)
                .useHttps(true)
                .responseTimeout(60)// 服务器响应超时。默认60秒
                //.zone(FixedZone.zone0) // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
                .zone(FixedZone.zone2) // 设置华南区域，指定不同区域的上传域名、备用域名、备用IP。 https://segmentfault.com/q/1010000008596429
                .build();

        mUploadManager = new UploadManager(config);


        mSelectImg.setOnClickListener(this);
        mRadio0.setOnClickListener(this);
        mRadio1.setOnClickListener(this);
        mRadio2.setOnClickListener(this);
        mRadio3.setOnClickListener(this);
        mRadio4.setOnClickListener(this);
        mRadio5.setOnClickListener(this);
        mRadio6.setOnClickListener(this);
        findViewById(R.id.release_btn).setOnClickListener(this);
        findViewById(R.id.button_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_0:
                type = 0;
                break;
            case R.id.radio_1:
                type = 1;
                break;
            case R.id.radio_2:
                type = 2;
                break;
            case R.id.radio_3:
                type = 3;
                break;
            case R.id.radio_4:
                type = 4;
                break;
            case R.id.radio_5:
                type = 5;
                break;
            case R.id.radio_6:
                type = 6;
                break;
            case R.id.select_img:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                        .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                        .maxSelectNum(1)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                        .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                        .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高,默认为false
                        .isAndroidQTransform(false)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .synOrAsy(false)//同步true或异步false 压缩 默认同步
                        .forResult(100);
                break;
            case R.id.release_btn:
                addData();
                break;
            case R.id.button_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 100:
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                LocalMedia mLocalMedia = selectList.get(0);
                imgUrl = mLocalMedia.getPath();
                GlideEngine.createGlideEngine().loadImage(this, imgUrl, mSelectImg);
                upFile(AppConstant.QINIUTOKEN, mLocalMedia.getPath());
                break;

        }
    }

    public void upFile(String qiNiuToken, String filePath) {
        try {
            if (!TextUtils.isEmpty(qiNiuToken)) {//web端返回的token=
                mUploadManager.put(new File(filePath)
                        , System.currentTimeMillis() + "", qiNiuToken
                        , (key, info, response) -> {
                            try {
                                imgUrl = AppConstant.IMG_URL + response.getString("key");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, null);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void addData() {
        if (TextUtils.isEmpty(imgUrl)) {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mEditDescribe.getText().toString())) {
            Toast.makeText(this, "请输入简介", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Looper.prepare();
            mDataBean = new DataBean(type, imgUrl, mEditDescribe.getText().toString(), mUserBean.id, 0);
            int dataInsert = DBService.getDbService().insertData(mDataBean);
            if (dataInsert == 1) {
                Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
                finish();
            }
            Looper.loop();
        }).start();
    }
}