package com.work.checkwork.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.work.checkwork.R;


public class AppUtil {

    public static final int placeholderSoWhite = R.drawable.default_user_avatar;
    public static final int errorSoWhite = R.drawable.default_user_avatar;

    /*
     *加载图片(默认)
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderSoWhite) //占位图
                .error(errorSoWhite)       //错误图
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(url).apply(options).into(imageView);

    }

} 