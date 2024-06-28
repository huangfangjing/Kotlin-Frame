package com.example.chartlibraby.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chartlibraby.R;
import com.example.chartlibraby.adapter.BaseRecyclerAdapter;
import com.example.chartlibraby.adapter.BaseViewHolder;
import com.example.chartlibraby.bean.RadarData;
import com.example.chartlibraby.bean.TwoLevelData;
import com.example.chartlibraby.databinding.AdapterAiViewIndicateLayoutBinding;
import com.example.chartlibraby.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AiRadarView extends FrameLayout {

    private RadarView mRadarView;
    private RecyclerView mRadarIndacation;
    private LinearLayout mLlContent, mLlEmpty;

    private int colors[] = new int[]{R.color.g20, R.color.g21, R.color.g22, R.color.g23, R.color.g24,
            R.color.g25, R.color.g26, R.color.g27, R.color.g28,R.color.bar1, R.color.bar2, R.color.bar3, R.color.bar4, R.color.bar5,
            R.color.bar6, R.color.bar7, R.color.bar8, R.color.bar9, R.color.bar10};

    public AiRadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.ai_radarview_layout, this);
        mRadarView = findViewById(R.id.radarView);
        mRadarView.setEmptyHint("暂无数据");
        mRadarView.setRotationEnable(false);
        mRadarIndacation = findViewById(R.id.radar_indication);
        mLlEmpty = findViewById(R.id.ll_empty);
        mLlContent = findViewById(R.id.ll_content);
    }

    public void buildData(List<TwoLevelData> evalScoreStatics, RadarView.RadarViewClickListener listener) {

        if (!CollectionUtil.isEmpty(evalScoreStatics) &&
                !CollectionUtil.isEmpty(evalScoreStatics.get(0).dataStatVoList)) {

            showContentView();

            mRadarView.clearRadarData();

            mRadarView.setRadarListener(listener);

            mRadarView.setMaxValue(getMaxmum(evalScoreStatics));

            List<Integer> layerColor = new ArrayList<>();
            Collections.addAll(layerColor, 0xfff2f7fe, 0xfff2f7fe, 0xfff2f7fe, 0xfff2f7fe, 0xfff2f7fe);
            mRadarView.setLayerColor(layerColor);

            List<String> vertexText = new ArrayList<>();
            List<String> indexNames = new ArrayList<>();
            for (int i = 0; i < evalScoreStatics.size(); i++) {
                vertexText.add(evalScoreStatics.get(i).getIndexName(10));
                indexNames.add(evalScoreStatics.get(i).indexName);
            }
            mRadarView.setVertexText(vertexText);
            mRadarView.setIndexName(indexNames);

            for (int i = 0; i < evalScoreStatics.get(0).dataStatVoList.size(); i++) {

                List<Float> values = new ArrayList<>();
                for (int j = 0; j < evalScoreStatics.size(); j++) {
                    List<TwoLevelData.DataStatVoListBean> dataStatVoList = evalScoreStatics.get(j).dataStatVoList;
                    if (i < dataStatVoList.size()) {
                        values.add(dataStatVoList.get(i).totalScore);
                    }
                }
                if (i < colors.length) {
                    RadarData data = new RadarData(evalScoreStatics.get(0).dataStatVoList.get(i).dataName,
                            values, getResources().getColor(colors[i]));
                    data.setValueTextEnable(false);
                    mRadarView.addData(data);
                }
            }
            mRadarView.setWebMode(RadarView.WEB_MODE_CIRCLE);

            mRadarIndacation.setVisibility(View.VISIBLE);
            //底部指示器
            List<TwoLevelData.DataStatVoListBean> voListBeans = evalScoreStatics.get(0).dataStatVoList;
            mRadarIndacation.setLayoutManager(new GridLayoutManager(getContext(), 1));
            List<List<TwoLevelData.DataStatVoListBean>> indicateLists = getIndicateList(voListBeans, 4);
            mRadarIndacation.setAdapter(new BaseRecyclerAdapter<List<TwoLevelData.DataStatVoListBean>>(getContext(), indicateLists, R.layout.adapter_ai_view_indicate_layout) {
                @Override
                public void setItemData(BaseViewHolder holder, int position, List<TwoLevelData.DataStatVoListBean> voListBeans) {

                    AdapterAiViewIndicateLayoutBinding binding = (AdapterAiViewIndicateLayoutBinding) holder.getBinding();
                    binding.llIndication.removeAllViews();
                    for (int i = 0; i < voListBeans.size(); i++) {
                        if (i <colors.length) {
                            ChartIndicateView indicateView = new ChartIndicateView(getContext());
                            TwoLevelData.DataStatVoListBean voListBean = voListBeans.get(i);
                            indicateView.setData(getResources().getColor(colors[i]),
                                    voListBean.getDataName(12), voListBean.isHide);
                            binding.llIndication.addView(indicateView);
                            indicateView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    voListBean.isHide = !voListBean.isHide;
                                    RadarData radarData = getTargetRader(voListBean.dataName);
                                    radarData.isHide = voListBean.isHide;
                                    mRadarView.invalidate();
                                    notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }


            });
        } else {
            showEmptyView();
            mRadarView.clearRadarData();
            mRadarIndacation.setVisibility(View.INVISIBLE);
        }
    }

    //获取目标rader
    private RadarData getTargetRader(String dataName) {
        if (dataName == null || CollectionUtil.isEmpty(mRadarView.getmRadarData())) {
            return null;
        }
        for (int i = 0; i < mRadarView.getmRadarData().size(); i++) {
            if (dataName.equals(mRadarView.getmRadarData().get(i).mLabel)) {
                return mRadarView.getmRadarData().get(i);
            }
        }
        return null;
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

    private float getMaxmum(List<TwoLevelData> evalScoreStatics) {
        List<Float> data = new ArrayList<>();
        for (int i = 0; i < evalScoreStatics.size(); i++) {
            List<TwoLevelData.DataStatVoListBean> dataStatVoList = evalScoreStatics.get(i).dataStatVoList;
            for (int j = 0; j < dataStatVoList.size(); j++) {
                data.add(evalScoreStatics.get(i).dataStatVoList.get(j).totalScore + 0.f);
            }
        }
        float max = Collections.max(data);
        if (max <= 5) {
            return 5.0f;
        }
        if (max <= 10) {
            return 10.0f;
        }
        if (max <= 15) {
            return 15.0f;
        }
        if (max <= 20) {
            return 20.0f;
        }
        if (max <= 30) {
            return 30.0f;
        }
        if (max <= 40) {
            return 50.0f;
        }
        if (max <= 50) {
            return 60.0f;
        }
        if (max <= 60) {
            return 80.0f;
        }
        if (max <= 100) {
            return 100.f;
        }
        return max + 10.f;
    }

    public void showContentView() {
        mLlContent.setVisibility(View.VISIBLE);
        mLlEmpty.setVisibility(View.GONE);
    }

    public void showEmptyView() {
        mLlContent.setVisibility(View.GONE);
        mLlEmpty.setVisibility(View.VISIBLE);
    }
}
