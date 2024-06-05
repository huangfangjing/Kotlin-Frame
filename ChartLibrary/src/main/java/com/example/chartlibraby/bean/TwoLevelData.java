package com.example.chartlibraby.bean;

import android.text.TextUtils;

import com.example.chartlibraby.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

public class TwoLevelData implements Serializable {


    public double avgScore;
    public List<DataStatVoListBean> dataStatVoList;
    public String indexId;
    public String indexName;

    public boolean isMyLocation;

    public static class DataStatVoListBean implements Serializable {
        public String dataId;
        public String dataName;
        public String dataType;
        public float totalScore;
        public String totalScoreStr;

        public int color;//本地属性
        public boolean isHide;//本地属性

        public DataStatVoListBean() {
        }

        public String getTotalScoreStr() {
            if (totalScoreStr == null) {
                return StringUtil.formatZeroDecimalPoint(totalScore);
            }
            return totalScoreStr;
        }

        public DataStatVoListBean(float totalScore) {
            this.totalScore = totalScore;
        }

        public DataStatVoListBean(String dataName, float totalScore) {
            this.dataName = dataName;
            this.totalScore = totalScore;
        }

        public DataStatVoListBean(String dataName, float totalScore, String totalScoreStr) {
            this.dataName = dataName;
            this.totalScore = totalScore;
            this.totalScoreStr = totalScoreStr;
        }

        public String getDataName(int length) {
            if (dataName.length() <= length) {
                return dataName;
            }
            return dataName.substring(0, length) + "...";
        }
    }

    public String getIndexName(int length) {
        if (TextUtils.isEmpty(indexName)){
            return "";
        }
        if (indexName.length() <= length) {
            return indexName;
        }
        return indexName.substring(0, length) + "...";
    }
}
