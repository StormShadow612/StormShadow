package com.wenhua.wenhua.adpters;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.wenhua.wenhua.R;
import com.wenhua.wenhua.models.imageLoader.AsyncImageLoader;
import com.wenhua.wenhua.models.imageLoader.ImageAndText;
import com.wenhua.wenhua.models.imageLoader.ViewCache;

import java.util.List;


/**
 * Created by Administrator on 2017/9/16.
 */

public class AllcameraAdpter extends ArrayAdapter<ImageAndText> {
    private ListView listView;
    private AsyncImageLoader asyncImageLoader;
    private List<ImageAndText> imageAndTexts;



    public AllcameraAdpter(Activity activity, List<ImageAndText> imageAndTexts, ListView listView) {
        super(activity, 0);
        this.imageAndTexts=imageAndTexts;
        this.listView = listView;
        asyncImageLoader = new AsyncImageLoader();
    }

    @Override
    public int getCount() {
        return imageAndTexts.size();
    }

    @Override
    public ImageAndText getItem(int position) {
        return imageAndTexts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();
        View rowView = convertView;
        ViewCache viewCache;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.allcamera_details, null);
            viewCache = new ViewCache(rowView);
            rowView.setTag(viewCache);

        } else {
            viewCache = (ViewCache) rowView.getTag();
        }
        ImageAndText imageAndText = getItem(position);
        String imageUrl = imageAndText.getImageUrl();
        ImageView imageView = viewCache.getImageView();
        imageView.setTag(imageUrl);
        Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrl, new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(imageDrawable);
                }
            }
        });
        if (cachedImage == null) {
            imageView.setImageResource(R.drawable.placeholder);
        } else {
            imageView.setImageDrawable(cachedImage);
        }
        TextView textView = viewCache.getTextView();
        textView.setText(imageAndText.getText());

        viewCache.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemListener.onItemClick(position);
            }
        });
        return rowView;
    }

    /**
     * 按钮的监听接口
     */
    public interface onItemListener {
        void onItemClick(int position);
    }

    private onItemListener mOnItemListener;

    public void setOnItemClickListener(onItemListener mOnItemListener) {
        this.mOnItemListener = mOnItemListener;
    }
}
