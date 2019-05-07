package com.huxiaozhu.recyclerviewlib.refach.loadingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huxiaozhu.recyclerviewlib.R;

public abstract class SimpleImageLoadingView extends PullToRefreshLoadingView {
    private ImageView iv_image;

    public SimpleImageLoadingView(@NonNull Context context) {
        super(context);
        this.init();
    }

    public SimpleImageLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public SimpleImageLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    protected void init() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.view_simple_image_loading, this, true);
        this.iv_image = findViewById(R.id.iv_image);
    }

    public ImageView getImageView() {
        return iv_image;
    }
}

