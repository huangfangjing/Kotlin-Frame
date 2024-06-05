package com.example.chartlibraby.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chartlibraby.R;
import com.example.chartlibraby.adapter.BaseRecyclerAdapter;
import com.example.chartlibraby.adapter.BaseViewHolder;
import com.example.chartlibraby.bean.TwoLevelData;
import com.example.chartlibraby.databinding.AdapterAiViewIndicateLayoutBinding;
import com.example.chartlibraby.utils.CollectionUtil;
import com.example.chartlibraby.utils.DensityUtil;
import com.example.chartlibraby.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AiVerticalBarView extends FrameLayout {

    public VerticalBarView barView;
    private LinearLayout mLlTitles;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tip, tip2;
    private RecyclerView mIndicateView;
    private LinearLayout mTvEmpty;
    private LinearLayout mLlContent;
    private HorizontalScrollView horizontalScrollView;
    private String[] yDatsa;

    private int maxLengthIndexName = 9;


    public AiVerticalBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.ai_vertical_view_layout, this);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv6 = view.findViewById(R.id.tv6);
        tip = view.findViewById(R.id.tip);
        tip2 = view.findViewById(R.id.tip2);
        mTvEmpty = view.findViewById(R.id.ll_empty);
        barView = view.findViewById(R.id.bar_view);
        mLlTitles = view.findViewById(R.id.ll_titles);
        mIndicateView = view.findViewById(R.id.ll_indicate_bottom);
        mLlContent = view.findViewById(R.id.ll_content);
        horizontalScrollView = view.findViewById(R.id.horizontal);
    }

    public void setYScore(String[] score) {
        tv6.setText(score[0]);
        tv5.setText(score[1]);
        tv4.setText(score[2]);
        tv3.setText(score[3]);
        tv2.setText(score[4]);
        tv1.setText(score[5]);
    }

    public void setMaxLengthIndexName(int maxLengthIndexName) {
        this.maxLengthIndexName = maxLengthIndexName;
    }

    public void buildDate(List<TwoLevelData> barDatas, VerticalBarView.OnVerticalBarClick click, String tipContent, String tipContent2) {
        buildDate(barDatas, click);
        tip.setText(tipContent);
        if (!CollectionUtil.isEmpty(barDatas) && barDatas.size() <=5 && barView.getMeasuredWidth() < DeviceUtils.getScreenWidth(getContext())) {
            ViewGroup.LayoutParams layoutParams = tip2.getLayoutParams();
            layoutParams.width = barView.getMeasuredWidth();
            tip2.setLayoutParams(layoutParams);
        }
        tip2.setText(tipContent2);
    }

    public void setUseAnimation(boolean useAnimation) {
        if (barView != null) {
            barView.setUseAnimation(useAnimation);
        }
    }

    public void hideIndicate() {
        mIndicateView.setVisibility(View.GONE);
    }

    public void buildDate(List<TwoLevelData> barDatas) {
        buildDate(barDatas,null);
    }

    public void buildDate(List<TwoLevelData> barDatas, VerticalBarView.OnVerticalBarClick click) {

        if (!CollectionUtil.isEmpty(barDatas) && !CollectionUtil.isEmpty(barDatas.get(0).dataStatVoList)) {

            showContentView();

            List<TwoLevelData.DataStatVoListBean> voListBeans = barDatas.get(0).dataStatVoList;
            for (int i = 0; i < voListBeans.size(); i++) {
                if (i < VerticalBarView.COLOR.length) {
                    voListBeans.get(i).color = VerticalBarView.COLOR[i];
                }
            }
            tip.setVisibility(View.VISIBLE);
            tip2.setVisibility(View.VISIBLE);
            String scores[] = getYScore(barDatas);
            setYScore(scores);

            barView.setBarDatas( true, barDatas, Double.parseDouble(scores[scores.length - 1]));

            barView.setVerticalBarClick(click);

            barView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int barViewWidth = barView.useWidth;//动态测量barView的高度

            mLlTitles.removeAllViews();
            LayoutParams params = (LayoutParams) mLlTitles.getLayoutParams();
            params.width = barViewWidth;

            mLlTitles.setLayoutParams(params);
            mLlTitles.setPadding(DensityUtil.dp2px(barView.BAR_Y_SPACE) / 2 - 6, 0, DensityUtil.dp2px(barView.BAR_Y_SPACE) / 2, 0);
            for (int i = 0; i < barDatas.size(); i++) {
                TwoLevelData barData = barDatas.get(i);
                TextView textView = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.weight = 1;
                textView.setLayoutParams(layoutParams);
                textView.setGravity(Gravity.CENTER);
                String indexName = barData.getIndexName(maxLengthIndexName);
                textView.setText(indexName);
                textView.setSingleLine(true);
                textView.setTextSize(10);
                textView.setTextColor(getResources().getColor(R.color.black2));
                mLlTitles.addView(textView);
            }

            //底部指示器
            mIndicateView.setLayoutManager(new GridLayoutManager(getContext(), 1));
            List<List<TwoLevelData.DataStatVoListBean>> indicateLists = getIndicateList(voListBeans, 4);
            mIndicateView.setAdapter(new BaseRecyclerAdapter<List<TwoLevelData.DataStatVoListBean>>(getContext(), indicateLists,
                    R.layout.adapter_ai_view_indicate_layout) {
                @Override
                public void setItemData(BaseViewHolder holder, int position, List<TwoLevelData.DataStatVoListBean> voListBeans) {

                    AdapterAiViewIndicateLayoutBinding binding = (AdapterAiViewIndicateLayoutBinding) holder.getBinding();
                    binding.llIndication.removeAllViews();
                    for (int i = 0; i < voListBeans.size(); i++) {
                        if (i < VerticalBarView.COLOR.length) {
                            ChartIndicateView indicateView = new ChartIndicateView(getContext());
                            indicateView.setData(getResources().getColor(voListBeans.get(i).color),
                                    voListBeans.get(i).getDataName(12));
                            binding.llIndication.addView(indicateView);
                        }
                    }
                }
            });
        } else {
            showEmptyView();
        }
    }

    private List<List<TwoLevelData.DataStatVoListBean>> getIndicateList(List<TwoLevelData.DataStatVoListBean> voListBeans, int count) {
        if (voListBeans.size() == 5) {
            count = 3;
        }
        List<List<TwoLevelData.DataStatVoListBean>> indicateLists = new ArrayList<>();
        List<TwoLevelData.DataStatVoListBean> beans = new ArrayList<>();
        for (int i = 0; i < voListBeans.size(); i++) {
            beans.add(voListBeans.get(i));
            if (i == voListBeans.size() - 1) {
                indicateLists.add(beans);
                break;
            }
            if (beans.size() == count) {
                indicateLists.add(beans);
                beans = new ArrayList<>();
            }
        }

        return indicateLists;
    }

    public void showEmptyView() {
        mTvEmpty.setVisibility(View.VISIBLE);
        mLlContent.setVisibility(View.GONE);
    }

    public void showContentView() {
        mTvEmpty.setVisibility(View.GONE);
        mLlContent.setVisibility(View.VISIBLE);
    }

    public void setDefaultYScore(String[] yDatsa) {
        this.yDatsa = yDatsa;
    }

    private String[] getYScore(List<TwoLevelData> evalScoreStatics) {
        if (yDatsa != null && yDatsa.length != 0) {
            return yDatsa;
        }
        List<Float> data = new ArrayList<>();
        for (int i = 0; i < evalScoreStatics.size(); i++) {
            List<TwoLevelData.DataStatVoListBean> dataStatVoList = evalScoreStatics.get(i).dataStatVoList;
            for (int j = 0; j < dataStatVoList.size(); j++) {
                data.add(evalScoreStatics.get(i).dataStatVoList.get(j).totalScore + 0.f);
            }
        }
        float max = Collections.max(data);
        if (max <= 5) {
            return new String[]{"0", 5 / 5 * 1 + "", 5 / 5 * 2 + "", 5 / 5 * 3 + "", 5 / 5 * 4 + "", 5 / 5 * 5 + ""};
        }
        if (max <= 10) {
            return new String[]{"0", 10 / 5 * 1 + "", 10 / 5 * 2 + "", 10 / 5 * 3 + "", 10 / 5 * 4 + "", 10 / 5 * 5 + ""};
        }
        if (max <= 15) {
            return new String[]{"0", 15 / 5 * 1 + "", 15 / 5 * 2 + "", 15 / 5 * 3 + "", 15 / 5 * 4 + "", 15 / 5 * 5 + ""};
        }
        if (max <= 20) {
            return new String[]{"0", 20 / 5 * 1 + "", 20 / 5 * 2 + "", 20 / 5 * 3 + "", 20 / 5 * 4 + "", 20 / 5 * 5 + ""};
        }
        if (max <= 30) {
            return new String[]{"0", 30 / 5 * 1 + "", 30 / 5 * 2 + "", 30 / 5 * 3 + "", 30 / 5 * 4 + "", 30 / 5 * 5 + ""};
        }
        if (max <= 40) {
            return new String[]{"0", 40 / 5 * 1 + "", 40 / 5 * 2 + "", 40 / 5 * 3 + "", 40 / 5 * 4 + "", 40 / 5 * 5 + ""};
        }
        if (max <= 50) {
            return new String[]{"0", 50 / 5 * 1 + "", 50 / 5 * 2 + "", 50 / 5 * 3 + "", 50 / 5 * 4 + "", 50 / 5 * 5 + ""};
        }
        if (max <= 60) {
            return new String[]{"0", 60 / 5 * 1 + "", 60 / 5 * 2 + "", 60 / 5 * 3 + "", 60 / 5 * 4 + "", 60 / 5 * 5 + ""};
        }
        if (max <= 80) {
            return new String[]{"0", 80 / 5 * 1 + "", 80 / 5 * 2 + "", 80 / 5 * 3 + "", 80 / 5 * 4 + "", 80 / 5 * 5 + ""};
        }
        if (max <= 100) {
            return new String[]{"0", 100 / 5 * 1 + "", 100 / 5 * 2 + "", 100 / 5 * 3 + "", 100 / 5 * 4 + "", 100 / 5 * 5 + ""};
        }
        return new String[]{"0", max / 5 * 1 + "", max / 5 * 2 + "", max / 5 * 3 + "", max / 5 * 4 + "", max / 5 * 5 + ""};
    }
}
