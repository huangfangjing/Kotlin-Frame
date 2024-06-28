package com.example.chartlibraby.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;

import com.example.chartlibraby.R;
import com.example.chartlibraby.bean.TwoLevelData;
import com.example.chartlibraby.utils.CollectionUtil;
import com.example.chartlibraby.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {

    private static final String TAG = "LineChartView";

    private Paint mLinePaint;
    private Paint LineChartPaint;
    private Paint dotPaint;

    private List<TwoLevelData> barDatas;
    private boolean isShowData;

    public static float BAR_INTERVAL = 50;//两点水平之间的间隔

    private double max = 100;//默认最大值为100

    private int downX;

    private boolean useAnimation = true;
    private float mask = 0;
    private static final int DRUING = 1200;

    public static int COLOR[] = {R.color.bar7, R.color.bar2, R.color.bar3, R.color.bar4, R.color.bar5,
            R.color.bar6, R.color.bar7, R.color.bar8, R.color.bar9, R.color.bar10, R.color.g20, R.color.g21, R.color.g22, R.color.g23, R.color.g24,
            R.color.g25, R.color.g26, R.color.g27, R.color.g28};

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDate();
    }


    public void setUseAnimation(boolean useAnimation) {
        this.useAnimation = useAnimation;
    }

    public void setBarDatas(boolean isShowData, List<TwoLevelData> barDatas) {

        if (CollectionUtil.isEmpty(barDatas) || barDatas.size() == 1) {
            return;
        }
        this.barDatas = barDatas;
        this.isShowData = isShowData;
        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        if (!CollectionUtil.isEmpty(barDatas) && barDatas.size() <= 5) {
            BAR_INTERVAL = 300 / barDatas.size();
        }
        if (useAnimation) {
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
            animator.setDuration(DRUING);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mask = (float) animation.getAnimatedValue() * mMeasureWidth;
                    requestLayout();
                    invalidate();
                }
            });
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();

        } else {
            mask = mMeasureWidth;
            requestLayout();
            invalidate();
        }
    }

    public void setBarDatas(boolean isShowData, List<TwoLevelData> barDatas, double max) {
        this.max = max;
        setBarDatas(isShowData, barDatas);
    }

    private void initDate() {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setColor(Color.LTGRAY);

        LineChartPaint = new Paint();
        LineChartPaint.setAntiAlias(true);
        LineChartPaint.setStrokeWidth(4);
        LineChartPaint.setStyle(Paint.Style.STROKE);
        LineChartPaint.setColor(Color.LTGRAY);

        dotPaint = new Paint();
        dotPaint.setAntiAlias(true);
    }

    protected int mMeasureWidth, mMeasureHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
        mMeasureHeight = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY && !CollectionUtil.isEmpty(barDatas) &&
                !CollectionUtil.isEmpty(barDatas.get(0).dataStatVoList)) {
            //重新计算宽度
            mMeasureWidth = (int) (barDatas.size() * DensityUtil.dp2px(BAR_INTERVAL));
        }
        setMeasuredDimension(mMeasureWidth, mMeasureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int spacialHeight = (mMeasureHeight - DensityUtil.dp2px(30)) / 5;

        if (!CollectionUtil.isEmpty(barDatas) && !CollectionUtil.isEmpty(barDatas.get(0).dataStatVoList)) {
            for (int i = 0; i < 6; i++) {
                //绘制横线
                canvas.drawLine(0, spacialHeight * i + DensityUtil.dp2px(30) - 1, mMeasureWidth,
                        spacialHeight * i + DensityUtil.dp2px(30) - 1, mLinePaint);
            }
            List<TwoLevelData.DataStatVoListBean> dataStatVoList = barDatas.get(0).dataStatVoList;

            //绘制所有折线
            for (int i = 0; i < dataStatVoList.size(); i++) {
                if (!dataStatVoList.get(i).isHide) {
                    List<Point> points = new ArrayList<>();
                    for (int j = 0; j < barDatas.size(); j++) {
                        TwoLevelData barData = barDatas.get(j);
                        if (j < COLOR.length) {
                            TwoLevelData.DataStatVoListBean voListBean = barData.dataStatVoList.get(i);
                            LineChartPaint.setColor(getContext().getResources().getColor(COLOR[i]));
                            float dotHeight = (float) ((mMeasureHeight - DensityUtil.dp2px(30)) * (voListBean.totalScore / max));
                            if (dotHeight < 10) {
                                dotHeight = 10;//防止在X坐标轴上的圆圈绘制显示不全
                            }
                            int x = (int) (DensityUtil.dp2px(BAR_INTERVAL) * (0.5 + j));
                            int y = (int) (mMeasureHeight - dotHeight);
                            points.add(new Point(x, y));
                            if (points.size() > 1) {
                                Point p1 = points.get(j - 1);
                                Point p2 = points.get(j);

                                if (p1.x > mask && useAnimation) break;
                                if (p2.x > mask && useAnimation) {
                                    p2.y = (int) (((p2.y - p1.y) * (mask - p1.x) / (p2.x - p1.x) + p1.y));
                                    p2.x = (int) mask;
                                    canvas.drawLine(p1.x, p1.y, p2.x, p2.y, LineChartPaint);
                                    break;
                                }
                                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, LineChartPaint);
                            }
                        }
                    }
                }
            }

            //为了绘制出折线的空心圆，只能先把所有的折线画完了再画圆
            for (int i = 0; i < dataStatVoList.size(); i++) {
                if (!dataStatVoList.get(i).isHide) {
                List<Point> points = new ArrayList<>();
                for (int j = 0; j < barDatas.size(); j++) {
                    TwoLevelData barData = barDatas.get(j);
                    if (j < COLOR.length) {
                        TwoLevelData.DataStatVoListBean voListBean = barData.dataStatVoList.get(i);
                            LineChartPaint.setColor(getContext().getResources().getColor(COLOR[i]));
                            float dotHeight = (float) ((mMeasureHeight - DensityUtil.dp2px(30)) * (voListBean.totalScore / max));
                            if (dotHeight < 10) {
                                dotHeight = 10;//防止在X坐标轴上的圆圈绘制显示不全
                            }
                            int x = (int) (DensityUtil.dp2px(BAR_INTERVAL) * (0.5 + j));
                            int y = (int) (mMeasureHeight - dotHeight);
                            points.add(new Point(x, y));
                            if (points.get(j).x > mask && useAnimation) break;
                            dotPaint.setColor(getContext().getResources().getColor(COLOR[i]));
                            canvas.drawCircle(points.get(j).x, points.get(j).y, 10, dotPaint);
                            dotPaint.setColor(getResources().getColor(R.color.white));
                            canvas.drawCircle(points.get(j).x, points.get(j).y, 6, dotPaint);
                        }
                    }
                }
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
                    if (chartListener != null) {
                        chartListener.onLineChartClick(barDatas.get(position));
                    }
                }
                break;
        }
        return true;
    }

    private int getClickPosition(int x) {
        for (int i = 0; i < barDatas.size(); i++) {
            if (x > DensityUtil.dp2px(BAR_INTERVAL) && x < (DensityUtil.dp2px(BAR_INTERVAL) * (i + 1))) {
                return i;
            }
        }
        return 0;
    }

    public interface OnLineChartListener {
        void onLineChartClick(TwoLevelData twoLevelData);
    }

    private OnLineChartListener chartListener;

    public void setLineChartListener(OnLineChartListener chartListener) {
        this.chartListener = chartListener;
    }
}
