package com.example.chartlibraby.bean;

import java.io.Serializable;
import java.util.List;

public class LineDataBean implements Serializable {


    public List<DataBean> data;
    public String message;
    public boolean state;

    public static class DataBean implements Serializable {
        private String axisName;
        private double classSoreRate;
        private String classSoreRateStr;
        private String scoreRateName;
        private double studentSoreRate;
        private String studentSoreRateStr;
    }
}
