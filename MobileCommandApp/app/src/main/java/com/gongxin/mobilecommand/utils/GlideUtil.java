package com.gongxin.mobilecommand.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gongxin.mobilecommand.R;

import java.io.File;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class GlideUtil {

    // 是否大屏
    public static void Load(ImageView image, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.image_loadfail);

        Glide.with(image.getContext())
                .load(url)
                .apply(options)
                .into(image);
    }

    // 圆四角
    public static void LoadRoundedCorners(ImageView image, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.image_loadfail)
                .bitmapTransform(new RoundedCornersTransformation(35, 5));

        Glide.with(image.getContext())
                .load(url)
                .apply(options)
                .into(image);
    }

    // 圆四角
    public static void LoadRoundedCorners(ImageView image, File url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.image_loadfail)
                .bitmapTransform(new RoundedCornersTransformation(15, 0));

        Glide.with(image.getContext())
                .load(url)
                .apply(options)
                .into(image);
    }

    // 圆角
    public static void LoadCircleCorners(ImageView image, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.head_default_ic)
                .bitmapTransform(new CircleCrop());
        //d
        Glide.with(image.getContext())
                .load(HttpUtil.BASEURL+url)
                .apply(options)
                .transition(withCrossFade())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(image);
    }

    // 圆角本地
    public static void LoadCircleCornersFromDisk(ImageView image, int rid) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.head_default_ic)
                .bitmapTransform(new CircleCrop());
        //d
        Glide.with(image.getContext())
                .load(rid)
                .apply(options)
                .transition(withCrossFade())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(image);
    }
}