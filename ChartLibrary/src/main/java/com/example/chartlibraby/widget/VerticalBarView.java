package com.example.chartlibraby.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


import com.example.chartlibraby.R;
import com.example.chartlibraby.bean.TwoLevelData;
import com.example.chartlibraby.utils.CollectionUtil;
import com.example.chartlibraby.utils.DensityUtil;
import com.example.chartlibraby.utils.DeviceUtils;

import java.util.List;

public class VerticalBarView extends View {

    private static final String TAG = "VerticalBarView";

    private Paint mLinePaint;
    private Paint mTopBarPaint;

    public static float BAR_Y_SPACE = 32;//bar水平间距
    public static float BAR_WIDTH = 12;//bar宽度
    public static float BAR_INTERVAL = 2;//bar之间的间隔

    private boolean useAnimation = true;
    private boolean showMyLocation = true;

    public static int COLOR[] = {R.color.bar1, R.color.bar2, R.color.bar3, R.color.bar4, R.color.bar5,
            R.color.bar6, R.color.bar7, R.color.bar8, R.color.bar9, R.color.bar10, R.color.g20, R.color.g21, R.color.g22, R.color.g23, R.color.g24,
            R.color.g25, R.color.g26, R.color.g27, R.color.g28};

    private List<TwoLevelData> barDatas;
    private boolean isShowData;

    private double max = 100;//默认最大值为100

    private int downX;

    public void setUseAnimation(boolean useAnimation) {
        this.useAnimation = useAnimation;
    }

    public void setBarDatas(boolean isShowData, List<TwoLevelData> barDatas) {
        this.barDatas = barDatas;
        this.isShowData = isShowData;
        if (!CollectionUtil.isEmpty(barDatas) && !CollectionUtil.isEmpty(barDatas.get(0).dataStatVoList)) {
            if (barDatas.get(0).dataStatVoList.size() == 1) {
                BAR_WIDTH = 22;
                COLOR[0] = R.color.g26;
            } else if (barDatas.get(0).dataStatVoList.size() == 2) {
                BAR_WIDTH = 18;
            }
        }
        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        invalidate();
        requestLayout();
    }

    public void setShowMyLocation(boolean showMyLocation) {
        this.showMyLocation = showMyLocation;
    }

    public void setBarDatas(boolean isShowData, List<TwoLevelData> barDatas, double max) {
        this.max = max;
        setBarDatas(isShowData, barDatas);
    }

    protected int mMeasureWidth, mMeasureHeight;

    public int useWidth;

    public VerticalBarView(Context context, @Nullable AttributeSet attrs) {
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
        mTopBarPaint.setTextAlign(Paint.Align.CENTER);
        mTopBarPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
        mMeasureHeight = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY && !CollectionUtil.isEmpty(barDatas) &&
                !CollectionUtil.isEmpty(barDatas.get(0).dataStatVoList)) {
            //重新计算宽度
            List<TwoLevelData.DataStatVoListBean> voListBeans = barDatas.get(0).dataStatVoList;
            useWidth = mMeasureWidth = DensityUtil.dp2px(BAR_WIDTH) * voListBeans.size() * barDatas.size() +
                    DensityUtil.dp2px(BAR_Y_SPACE) * (barDatas.size() + 1) +
                    DensityUtil.dp2px(BAR_INTERVAL) * (voListBeans.size() - 1) * barDatas.size();

            if (!CollectionUtil.isEmpty(barDatas) && barDatas.size() <= 4 && mMeasureWidth < DeviceUtils.getScreenWidth(getContext())) {
                mMeasureWidth = DeviceUtils.getScreenWidth(getContext()) - DensityUtil.dp2px(60);
            }
        }
        setMeasuredDimension(mMeasureWidth, mMeasureHeight);
    }

    int tempData = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int spacialHeight = (mMeasureHeight - DensityUtil.dp2px(36)) / 5;

        if (!CollectionUtil.isEmpty(barDatas) && !CollectionUtil.isEmpty(barDatas.get(0).dataStatVoList)) {
            int evenBarWidth = DensityUtil.dp2px(BAR_WIDTH) * barDatas.get(0).dataStatVoList.size() +
                    DensityUtil.dp2px(BAR_INTERVAL) * (barDatas.get(0).dataStatVoList.size() - 1);

            for (int i = 0; i < 6; i++) {
                //绘制横线
                canvas.drawLine(0, spacialHeight * i + DensityUtil.dp2px(36) - 1, mMeasureWidth,
                        spacialHeight * i + DensityUtil.dp2px(36) - 1, mLinePaint);
            }

            double rate = (++tempData) * 0.02;
            if (rate > 1 || !useAnimation) {
                rate = 1;
            }

            for (int i = 0; i < barDatas.size(); i++) {
                //绘制bar
                TwoLevelData barData = barDatas.get(i);
                for (int j = 0; j < barData.dataStatVoList.size(); j++) {
                    if (j < COLOR.length) {
                        TwoLevelData.DataStatVoListBean voListBean = barData.dataStatVoList.get(j);
                        mTopBarPaint.setColor(getContext().getResources().getColor(COLOR[j]));
                        double barHeight = (float) ((mMeasureHeight - DensityUtil.dp2px(36)) * (voListBean.totalScore / max)) * rate;
                        if (barHeight < 5 && barData.dataStatVoList.size() != 1) {
                            barHeight = 5;//给个最小值
                        }
                        canvas.drawRect(DensityUtil.dp2px(BAR_Y_SPACE) * (i + 1) + evenBarWidth * i + DensityUtil.dp2px(BAR_WIDTH) * j +
                                        DensityUtil.dp2px(BAR_INTERVAL) * (j - 1), (float) (mMeasureHeight - barHeight),
                                DensityUtil.dp2px(BAR_Y_SPACE) * (i + 1) + DensityUtil.dp2px(BAR_WIDTH) * (j + 1) +
                                        DensityUtil.dp2px(BAR_INTERVAL) * (j - 1) + evenBarWidth * i, mMeasureHeight, mTopBarPaint);
                        if (isShowData) {
                            //绘制条形y值
                            mTopBarPaint.setColor(getContext().getResources().getColor(R.color.black3));
                            canvas.drawText(voListBean.getTotalScoreStr(), DensityUtil.dp2px(BAR_Y_SPACE) * (i + 1) +
                                            DensityUtil.dp2px(BAR_WIDTH) * j + DensityUtil.dp2px(BAR_INTERVAL) * (j - 1)
                                            + DensityUtil.dp2px(BAR_WIDTH) / 2 + evenBarWidth * i,
                                    (float) (mMeasureHeight - barHeight - 12), mTopBarPaint);
                        }
                        if (showMyLocation && barData.isMyLocation) {
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my_location, null);
                            int bitmapWidth = bitmap.getWidth();
                            float y = (float) (mMeasureHeight - barHeight - 60 - bitmap.getHeight());
                            if (y < 0) {
                                y = 0;
                            }
                            canvas.drawBitmap(bitmap, DensityUtil.dp2px(BAR_Y_SPACE) * (i + 1) +
                                    DensityUtil.dp2px(BAR_WIDTH) * j + DensityUtil.dp2px(BAR_INTERVAL) * (j - 1)
                                    - ((bitmapWidth - DensityUtil.dp2px(BAR_WIDTH)) / 2)
                                    + evenBarWidth * i, y, mTopBarPaint);
                        }
                    }
                }
            }

            if (tempData <= 50 && useAnimation) {
                postInvalidate();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;

            case MotionEvent.ACTION_UP:
                if (CollectionUtil.isEmpty(barDatas)) {
                    break;
                }
                x = (int) event.getX();
                int position = getClickPosition(x);
                if (position >= 0 && position < barDatas.size() && Math.abs(downX - x) <= 10) {
                    if (horizonBarClick != null) {
                        horizonBarClick.VerticalBarClick(position, barDatas.get(position));
                    }
                }
                break;
        }
        return true;
    }

    private int getClickPosition(int x) {
        int itemWidth = DensityUtil.dp2px(BAR_WIDTH) * barDatas.get(0).dataStatVoList.size() +
                DensityUtil.dp2px(BAR_INTERVAL) * (barDatas.get(0).dataStatVoList.size() - 1);
        int space = DensityUtil.dp2px(BAR_Y_SPACE);
        for (int i = 0; i < barDatas.size(); i++) {
            if (x > space + i * (itemWidth + space) && x < space + i * (itemWidth + space) + itemWidth) {
                return i;
            }
        }
        return -1;
    }

    public interface OnVerticalBarClick {
        void VerticalBarClick(int position, TwoLevelData levelData);
    }

    private OnVerticalBarClick horizonBarClick;

    public void setVerticalBarClick(OnVerticalBarClick horizonBarClick) {
        this.horizonBarClick = horizonBarClick;
    }
}
