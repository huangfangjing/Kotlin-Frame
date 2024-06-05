package com.example.chartlibraby.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chartlibraby.R;


public class ChartIndicateView extends FrameLayout {

    public ImageView mImageView;
    public TextView mTextView;

    public ChartIndicateView(@NonNull Context context) {
        this(context, null);
    }

    public ChartIndicateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.chart_bottom_indicate, this);
        mImageView = view.findViewById(R.id.view);
        mTextView = view.findViewById(R.id.tv_name);
    }

    public void setData(int color, String text) {
        mImageView.setBackgroundColor(color);
        mTextView.setText(text);
    }

    public void setData(int color, String text, boolean isHide) {
        mImageView.setBackgroundColor(color);
        mTextView.setText(text);
        mImageView.setBackgroundColor(isHide ? getResources().getColor(R.color.ai_line2) : color);
        mTextView.setTextColor(getResources().getColor(isHide ? R.color.ai_line2 : R.color.black3));
    }
}
