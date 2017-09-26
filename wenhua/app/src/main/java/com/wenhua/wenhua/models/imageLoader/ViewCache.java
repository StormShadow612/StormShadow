package com.wenhua.wenhua.models.imageLoader;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenhua.wenhua.R;

/**
 * Created by Administrator on 2017/9/20.
 */

public class ViewCache {
    private View baseView;
    private TextView textView;
    private ImageView imageView;
    public Button button;

    public ViewCache(View baseView) {
        this.baseView = baseView;
    }

    public TextView getTextView() {
        if (textView == null) {
            textView = (TextView) baseView.findViewById(R.id.infoCamera);
        }
        return textView;
    }

    public ImageView getImageView() {
        if (imageView == null) {
            imageView = (ImageView) baseView.findViewById(R.id.imageCamera);
        }
        return imageView;
    }

    public Button getButton() {
        if (button == null) {
            button = (Button) baseView.findViewById(R.id.btnCamera);
        }
        return button;
    }
}
