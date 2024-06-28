package com.example.chartlibraby.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.example.chartlibraby.utils.AppUtil;
import com.example.chartlibraby.utils.CollectionUtil;
import com.example.chartlibraby.utils.DensityUtil;
import com.example.chartlibraby.utils.ScreenUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AiHorizonBarView extends FrameLayout {

    private HorizontalBarView barView;
    private LinearLayout mLlTitles;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tip;
    private RecyclerView mIndicateView;
    private LinearLayout mTvEmpty;
    private LinearLayout mLlContent, mLlBar;

    public AiHorizonBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.ai_horizon_view_layout, this);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv6 = view.findViewById(R.id.tv6);
        tip = view.findViewById(R.id.tip);
        mTvEmpty = view.findViewById(R.id.ll_empty);
        barView = view.findViewById(R.id.bar_view);
        mLlTitles = view.findViewById(R.id.ll_titles);
        mIndicateView = view.findViewById(R.id.ll_indicate_bottom);
        mLlBar = view.findViewById(R.id.ll_bar);
        mLlContent = view.findViewById(R.id.ll_content);
    }

    public void setYScore(String[] score) {
        tv1.setText(score[0]);
        tv2.setText(score[1]);
        tv3.setText(score[2]);
        tv4.setText(score[3]);
        tv5.setText(score[4]);
        tv6.setText(score[5]);
    }

    public void buildDate(List<TwoLevelData> barDatas, HorizontalBarView.OnHorizonBarClick click, String tipContent) {
        buildDate(barDatas, click);
        tip.setText(tipContent);
    }

    public void buildDate(List<TwoLevelData> barDatas, HorizontalBarView.OnHorizonBarClick click) {

        String s = new Gson().toJson(barDatas);
        Log.e("taggg",s);

        if (!CollectionUtil.isEmpty(barDatas) && !CollectionUtil.isEmpty(barDatas.get(0).dataStatVoList)) {

            showContentView();

            List<TwoLevelData.DataStatVoListBean> voListBeans = barDatas.get(0).dataStatVoList;
            for (int i = 0; i < voListBeans.size(); i++) {
                if (i < HorizontalBarView.COLOR.length) {
                    voListBeans.get(i).color = HorizontalBarView.COLOR[i];
                }
            }

            tip.setVisibility(View.VISIBLE);
            String scores[] = getYScore(barDatas);
            setYScore(scores);
            int screenWidth = ScreenUtils.getScreenWidth(getContext()) - DensityUtil.dp2px(30);
            LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(screenWidth * 5 / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
            mLlBar.setLayoutParams(barParams);
            barView.setBarDatas(true, barDatas, Double.parseDouble(scores[scores.length - 1]));

            barView.setHorizonBarClick(click);

            barView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int barViewheight = barView.getMeasuredHeight();//动态测量barView的高度

            int textViewHeight = AppUtil.dip2px(getContext(), HorizontalBarView.BAR_WIDTH * voListBeans.size() +
                    HorizontalBarView.BAR_INTERVAL * (voListBeans.size() - 1));

            mLlTitles.removeAllViews();
            ViewGroup.LayoutParams params = mLlTitles.getLayoutParams();
            params.height = barViewheight;
            mLlTitles.setLayoutParams(params);
            for (int i = 0; i < barDatas.size(); i++) {
                TwoLevelData barData = barDatas.get(i);
                TextView textView = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, textViewHeight);
                if (barDatas.size() != 1 && i != barDatas.size() - 1) {
                    layoutParams.setMargins(0, 0, 0, DensityUtil.dp2px(HorizontalBarView.BAR_Y_SPACE));
                }
                textView.setLayoutParams(layoutParams);
                textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                textView.setText(barData.getIndexName(12));
                textView.setSingleLine(true);
                textView.setTextSize(12);
                textView.setTextColor(getResources().getColor(R.color.black2));
                mLlTitles.addView(textView);
            }

            //底部指示器
            mIndicateView.setLayoutManager(new GridLayoutManager(getContext(), 1));
            List<List<TwoLevelData.DataStatVoListBean>> indicateLists = getIndicateList(voListBeans, 4);
            mIndicateView.setAdapter(new BaseRecyclerAdapter<List<TwoLevelData.DataStatVoListBean>>(getContext(), indicateLists, R.layout.adapter_ai_view_indicate_layout) {
                @Override
                public void setItemData(BaseViewHolder holder, int position, List<TwoLevelData.DataStatVoListBean> voListBeans) {

                    AdapterAiViewIndicateLayoutBinding binding = (AdapterAiViewIndicateLayoutBinding) holder.getBinding();
                    binding.llIndication.removeAllViews();
                    for (int i = 0; i < voListBeans.size(); i++) {
                        if (i < HorizontalBarView.COLOR.length) {
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

    private void showEmptyView() {
        mTvEmpty.setVisibility(View.VISIBLE);
        mLlContent.setVisibility(View.GONE);
    }

    private void showContentView() {
        mTvEmpty.setVisibility(View.GONE);
        mLlContent.setVisibility(View.VISIBLE);
    }

    private String[] getYScore(List<TwoLevelData> evalScoreStatics) {
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
