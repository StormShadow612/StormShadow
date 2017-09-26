package com.wenhua.wenhua.models.imageLoader;

/**
 * Created by Administrator on 2017/9/20.
 */

public class ImageAndText {
    private String imageUrl;
    private String text;

    public ImageAndText(String imageUrl, String text) {
        this.imageUrl = imageUrl;
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setText(String text) {
        this.text = text;
    }
}
