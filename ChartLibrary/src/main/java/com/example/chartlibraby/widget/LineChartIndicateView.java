package com.example.chartlibraby.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class LineChartIndicateView extends ChartIndicateView {

    public LineChartIndicateView(@NonNull Context context) {
        super(context);
    }

    public LineChartIndicateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(int color, String text) {
        mImageView.setImageResource(color);//这里color其实是传入了图片resourceId
        mTextView.setText(text);
    }
}
