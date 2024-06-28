package com.example.chartlibraby.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.chartlibraby.R;
import com.example.chartlibraby.bean.TwoLevelData;
import com.example.chartlibraby.utils.AppUtil;
import com.example.chartlibraby.utils.CollectionUtil;
import com.example.chartlibraby.utils.DensityUtil;
import com.example.chartlibraby.utils.StringUtil;

import java.util.List;


public class HorizontalBarView extends View {

    private static final String TAG = "HorizontalBarView";

    private Paint mLinePaint;
    private Paint mTopBarPaint;

    public static float BAR_Y_SPACE = 30;//bar垂直间距
    public static float BAR_WIDTH = 12;//bar宽度
    public static float BAR_INTERVAL = 5;//bar之间的间隔


    public static int COLOR[] = {R.color.bar7, R.color.bar2, R.color.bar3, R.color.bar4, R.color.bar5,
            R.color.bar6, R.color.bar7, R.color.bar8, R.color.bar9, R.color.bar10, R.color.g20, R.color.g21, R.color.g22, R.color.g23, R.color.g24,
            R.color.g25, R.color.g26, R.color.g27, R.color.g28};

    private List<TwoLevelData> barDatas;
    private boolean isShowData;

    private double max = 100;//默认最大值为100

    private int downY;


    public void setBarDatas(boolean isShowData, List<TwoLevelData> barDatas) {
        this.barDatas = barDatas;
        this.isShowData = isShowData;
        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        invalidate();
        requestLayout();
    }

    public void setBarDatas(boolean isShowData, List<TwoLevelData> barDatas, double max) {
        this.max = max;
        if (!CollectionUtil.isEmpty(barDatas) && !CollectionUtil.isEmpty(barDatas.get(0).dataStatVoList)
                && barDatas.get(0).dataStatVoList.size() == 1) {
            BAR_WIDTH = 15;
        }
        setBarDatas(isShowData, barDatas);
    }

    protected int mMeasureWidth, mMeasureHeight;


    public HorizontalBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDate();
    }

    private void initDate() {

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setColor(Color.LTGRAY);

        mTopBarPaint = new Paint();
        mTopBarPaint.setTextSize(30);
        mTopBarPaint.setAntiAlias(true);
        mTopBarPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
        mMeasureHeight = MeasureSpec.getSize(heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY && !CollectionUtil.isEmpty(barDatas) &&
                !CollectionUtil.isEmpty(barDatas.get(0).dataStatVoList)) {
            //重新计算高度
            List<TwoLevelData.DataStatVoListBean> voListBeans = barDatas.get(0).dataStatVoList;
            mMeasureHeight = AppUtil.dip2px(getContext(), BAR_WIDTH) * voListBeans.size() * barDatas.size() +
                    AppUtil.dip2px(getContext(), BAR_Y_SPACE) * (barDatas.size() - 1) +
                    AppUtil.dip2px(getContext(), BAR_INTERVAL) * (voListBeans.size() - 1) * barDatas.size();
        }

        setMeasuredDimension(mMeasureWidth, mMeasureHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int spacialWidth = mMeasureWidth - DensityUtil.dp2px(36);

        if (!CollectionUtil.isEmpty(barDatas) && !CollectionUtil.isEmpty(barDatas.get(0).dataStatVoList)) {

            float spaceX = spacialWidth / 5;//0,20,40,60,80,100---默认5个梯度
            float spaceY = AppUtil.dip2px(getContext(), BAR_WIDTH) * barDatas.get(0).dataStatVoList.size()
                    + AppUtil.dip2px(getContext(), BAR_Y_SPACE) + AppUtil.dip2px(getContext(), BAR_INTERVAL) * (barDatas.get(0).dataStatVoList.size() - 1);


            for (int i = 0; i < 6; i++) {
                //绘制竖线
                canvas.drawLine(spaceX * i, 0, spaceX * i, mMeasureHeight, mLinePaint);
            }

            for (int i = 0; i < barDatas.size(); i++) {
                //绘制bar
                TwoLevelData barData = barDatas.get(i);
                for (int j = 0; j < barData.dataStatVoList.size(); j++) {
                    if (j < COLOR.length) {
                        TwoLevelData.DataStatVoListBean voListBean = barData.dataStatVoList.get(j);
                        mTopBarPaint.setColor(getContext().getResources().getColor(COLOR[j]));

                        float right = (float) (spacialWidth * (voListBean.totalScore / max));
                        if (right < 20) {
                            right = 20;//给个最小值
                        }
                        canvas.drawRect(0, AppUtil.dip2px(getContext(), BAR_WIDTH) * j + AppUtil.dip2px(getContext(), BAR_INTERVAL) * j + spaceY * i, right,
                                AppUtil.dip2px(getContext(), BAR_WIDTH) * (j + 1) + spaceY * i + AppUtil.dip2px(getContext(), BAR_INTERVAL) * j, mTopBarPaint);
                        if (isShowData) {
                            //绘制条形y值
                            canvas.drawText(StringUtil.formatZeroDecimalPoint(voListBean.totalScore), right + 15,
                                    AppUtil.dip2px(getContext(), BAR_WIDTH) * j + spaceY * i + 27 + AppUtil.dip2px(getContext(), BAR_INTERVAL) * j, mTopBarPaint);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int y;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getY();
                break;

            case MotionEvent.ACTION_UP:
                if (CollectionUtil.isEmpty(barDatas)) {
                    break;
                }
                y = (int) event.getY();
                int position = getClickPosition(y);
                if (position >= 0 && position < barDatas.size() && Math.abs(downY - y) <= 10) {
                    if (horizonBarClick != null) {
                        horizonBarClick.horizonBarClick(barDatas.get(position));
                    }
                }
                break;
        }
        return true;
    }

    private int getClickPosition(int x) {
        int itemHeight = (int) (barDatas.get(0).dataStatVoList.size() * AppUtil.dip2px(getContext(), BAR_WIDTH) +
                (barDatas.get(0).dataStatVoList.size() - 1) * AppUtil.dip2px(getContext(), BAR_INTERVAL));
        int space = AppUtil.dip2px(getContext(), BAR_Y_SPACE);
        for (int i = 0; i < barDatas.size(); i++) {
            if (x > i * (itemHeight + space) && x < i * (itemHeight + space) + itemHeight) {
                return i;
            }
        }
        return -1;
    }

    public interface OnHorizonBarClick {
        void horizonBarClick(TwoLevelData levelData);
    }

    private OnHorizonBarClick horizonBarClick;

    public void setHorizonBarClick(OnHorizonBarClick horizonBarClick) {
        this.horizonBarClick = horizonBarClick;
    }
}
