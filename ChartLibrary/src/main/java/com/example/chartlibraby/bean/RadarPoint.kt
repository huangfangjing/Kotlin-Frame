package com.example.chartlibraby.bean;

import android.graphics.Point;

public class RadarPoint {

    public Point mPoint;
    public String indexName;
    public float score;

    public RadarPoint(Point mPoint, String indexName, float score) {
        this.mPoint = mPoint;
        this.indexName = indexName;
        this.score = score;
    }
}
