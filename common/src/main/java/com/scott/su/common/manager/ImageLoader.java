package com.scott.su.common.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.scott.su.common.R;


public final class ImageLoader {
    private static final int DEFAULT_PLACEHOLDER = R.color.md_grey_200;
    private static final int DEFAULT_ERROR = R.drawable.ic_placeholder_image_error;


    public static void load(Context context, Object source, ImageView target) {
        load(context, source, target, DEFAULT_PLACEHOLDER, DEFAULT_ERROR, true);
    }

    public static void load(Context context, Object source, ImageView target, boolean anim) {
        load(context, source, target, DEFAULT_PLACEHOLDER, DEFAULT_ERROR, anim);
    }

    public static void load(Context context, Object source, ImageView target,
                            @DrawableRes int placeholder, @DrawableRes int error, boolean anim) {
        RequestOptions options = new RequestOptions();
        options = options.placeholder(placeholder)
                .error(error);

        if (!anim) {
            options = options.dontAnimate();
        }

        RequestBuilder<Drawable> builder = Glide.with(context)
                .load(source).apply(options);

        if (anim) {
            builder = builder.transition(new DrawableTransitionOptions().crossFade());
        }

        builder.into(target);
    }


}
