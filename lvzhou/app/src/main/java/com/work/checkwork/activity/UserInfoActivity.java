package com.work.checkwork.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.work.checkwork.dao.UserOpe;
import com.work.checkwork.model.UserBean;
import com.work.checkwork.preferences.AppPreferences;
import com.work.checkwork.utils.AppConstant;
import com.work.checkwork.utils.AppUtil;
import com.work.checkwork.utils.GlideEngine;

import org.json.JSONException;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class UserInfoActivity extends BaseActivity {

    private static final String TAG = "UserInfoActivity";

    private TextView mTitle;
    private ImageView userPhotoView;
    private TextView teacher_name;
    private AlertDialog.Builder builder;
    private UserBean mUserBean;
    private UploadManager mUploadManager;
    private String mUserPic;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void onViewCreated() {
        mUserBean = AppPreferences.getParcelableEntity(this, AppConstant.EXTRA_USERINFO, UserBean.class);

        mTitle = findViewById(R.id.textview_title);
        mTitle.setText("个人信息");
        userPhotoView = findViewById(R.id.user_photo_view);
        teacher_name = findViewById(R.id.teacher_name);

        AppUtil.loadImage(this, mUserBean.userPic, userPhotoView);
        teacher_name.setText(mUserBean.userName);

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

        userPhotoView.setOnClickListener(this);
        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.name_layout).setOnClickListener(this);
        findViewById(R.id.pwd_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                finish();
                break;
            case R.id.user_photo_view:
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
                        .forResult(102);
                break;
            case R.id.name_layout:
                showInput("昵称", mUserBean.userName, 0);
                break;
            case R.id.pwd_layout:
                showInput();
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
            case 102:
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                LocalMedia mLocalMedia = selectList.get(0);
                mUserPic = mLocalMedia.getPath();
                AppUtil.loadImage(this, mUserPic, userPhotoView);
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
                                mUserPic = AppConstant.IMG_URL + response.getString("key");
                                upUserInfo(mUserBean.userName, mUserBean.userPwd, mUserPic);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                upUserInfo(mUserBean.userName, mUserBean.userPwd, mUserPic);
                            }
                        }, null);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    /**
     * 一个输入框的 dialog
     */
    private void showInput(String title, String hint, int type) {
        final EditText editText = new EditText(this);
        if (type == 1) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        editText.setHint(hint);
        builder = new AlertDialog.Builder(this).setTitle("请输入" + title).setView(editText)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    switch (type) {
                        case 0:
                            upUserInfo(editText.getText().toString(), mUserBean.userPwd, mUserBean.userPic);
                            break;
                        case 1:
                            upUserInfo(mUserBean.userName, editText.getText().toString(), mUserBean.userPic);
                            break;
                    }
                });
        builder.show();
    }

    /**
     * 一个输入框的 dialog
     */
    private void showInput() {
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setHint("请输入旧密码");
        builder = new AlertDialog.Builder(this).setTitle("请输入旧密码").setView(editText)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    check(editText.getText().toString());
                });
        builder.show();
    }

    private void check(String pwd) {
        new Thread(() -> {
            UserBean u = UserOpe.getUserOpe().getUserPwd(mUserBean.id, pwd);
            if (u != null) {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }


    private void upUserInfo(String username, String password, String userpic) {
        new Thread(() -> {
            UserOpe.getUserOpe().updateUserData(username, password, userpic, mUserBean.id);
            mUserBean = UserOpe.getUserOpe().getUserData(mUserBean.id);
            AppPreferences.setBeanByFastJson(this, AppConstant.EXTRA_USERINFO, mUserBean);
            Message msg = handler.obtainMessage();
            msg.what = 0;
            msg.obj = mUserBean;
            handler.sendMessage(msg);
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    mUserBean = (UserBean) msg.obj;
                    teacher_name.setText(mUserBean.userName);
                    Toast.makeText(UserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    showInput("新密码", "请输入新密码", 1);
                    break;
                default:
                    break;
            }
        }
    };

}